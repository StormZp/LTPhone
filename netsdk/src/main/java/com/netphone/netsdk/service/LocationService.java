package com.netphone.netsdk.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;

import com.netphone.netsdk.socket.TcpSocket;
import com.netphone.netsdk.utils.CmdUtils;
import com.netphone.netsdk.utils.LogUtil;

/**
 * Created by lgp on 2017/8/31.
 */

public class LocationService extends Service {

    private Context context;
    public static double distance = 0.01;//10m\
    public static boolean             isLocation;//是否有定位
    public static boolean             isNetLocation;//基站定位
    private       LocationManager     locationManager;
    private       NotificationManager notificationManager;
    private       long   currentTime = 0;
    // 纬度
    public static double latitude    = 0.0;
    // 经度
    public static double longitude   = 0.0;
    private final String TAG         = LocationService.class.getCanonicalName();

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.error("LocationService", "60\tonStartCommand()\n" + "定位进来了");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 获取location对象
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            isLocation = false;
            return super.onStartCommand(intent, flags, startId);
        }
        isLocation = true;
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) && locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10 * 1000, 1f, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                checkDistance(location);
            } else {

                if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 1f, locationListener);
                Location location2 = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location2 != null) {
                    checkDistance(location2);
                }
            }
        } else {
            if (locationManager.getAllProviders().contains(LocationManager.NETWORK_PROVIDER))
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10 * 1000, 1f, locationListener);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null) {
                checkDistance(location);
            }
        }
        isNetLocation = true;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isNetLocation) {
                    getLocation();
                    SystemClock.sleep(10 * 1000);
                }
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    private final LocationListener locationListener = new LocationListener() {
        //当坐标改变时触发此函数，如果Provider传进相同的坐标，它就不会被触发
        public void onLocationChanged(Location location) {
            // log it when the location changes
            if (location != null) {
                checkDistance(location);
            }
        }

        // Provider被disable时触发此函数，比如GPS被关闭
        public void onProviderDisabled(String provider) {

        }

        //  Provider被enable时触发此函数，比如GPS被打开
        public void onProviderEnabled(String provider) {
        }

        // Provider的在可用、暂时不可用和无服务三个状态直接切换时触发此函数
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
            locationManager = null;
        }
        isNetLocation = false;
    }

    private synchronized void checkDistance(Location location) {
        if (location != null) {
            float[] results = new float[1];
            Location.distanceBetween(location.getLatitude(),
                    location.getLongitude(), latitude,
                    longitude, results);
            float result = (results[0] / 1000);//km
            LogUtil.error("LocationService", "145\tcheckDistance()\n" + "位置距离:" + result + "\tlatitude:" + latitude + "\tlongitude:" + longitude);
            //发送广播上传一次GPS

            latitude = location.getLatitude();
            longitude = location.getLongitude();
//                sendBroadcast(new Intent(BroadcastUtils.UPLOAD_GPS)
//                        .putExtra("longitude", String.valueOf(longitude))
//                        .putExtra("latitude", String.valueOf(latitude)));

            if (longitude == 0.0 && latitude == 0.0)
                return;
            {
                long l = System.currentTimeMillis();
                if ((currentTime + 60 * 1000) < l) {
                    currentTime = l;
                    byte[] datas = CmdUtils.getInstance().uploadGPS(String.valueOf(longitude), String.valueOf(latitude));
                    TcpSocket.getInstance().addData(datas);
                }
            }

        }
    }

    /**
     * 主动获取Location，通过以下方法获取到的是最后一次定位信息。
     * 注意：Location location=new Location(LocationManager.GPS_PROVIDER)方式获取的location的各个参数值都是为0。
     */
    public void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        if (locationManager != null) {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            checkDistance(location);
        }
//        Log4Lsy.i(TAG, "纬度："+location.getLatitude());
//        Log4Lsy.i(TAG, "经度："+location.getLongitude());
//        Log4Lsy.i(TAG, "海拔："+location.getAltitude());
//        Log4Lsy.i(TAG, "时间："+location.getTime());

    }
}
