package com.netphone.netsdk;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;

import com.netphone.gen.DaoMaster;
import com.netphone.gen.DaoSession;
import com.netphone.gen.GroupInfoBeanDao;
import com.netphone.netsdk.Tool.Constant;
import com.netphone.netsdk.Tool.TcpCmd;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.listener.OnNetworkListener;
import com.netphone.netsdk.service.NetworkConnectChangedReceiver;
import com.netphone.netsdk.service.SocketManageService;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.netsdk.utils.MySQLiteOpenHelper;
import com.netphone.netsdk.utils.SharedPreferenceUtil;

/**
 * Created Storm
 * Time    2018/4/13 11:07
 * Message {这是力同的管理类}
 */
public class LTConfigure {
    private static LTApi ltApi;
    public OnNetworkListener mOnNetworkListener;


    private static LTConfigure mlt;
    private static Context mContext;
    private static NetworkConnectChangedReceiver mNetworkConnectChangedReceiver;

    /**
     * sdk初始化
     *
     * @param context
     */
    public static void init(Context context) {
        mContext = context;
        if (mlt == null) {
            mlt = new LTConfigure();
        }
        initReceiver();
        initSocket();
        setDatabase();
        SharedPreferenceUtil.Companion.init(mContext);
        ltApi = LTApi.newInstance();
    }


    private static void initSocket() {
        mContext.startService(new Intent(mContext, SocketManageService.class));
    }

    /**
     * 初始化广播接收
     */
    private static void initReceiver() {
        mNetworkConnectChangedReceiver = new NetworkConnectChangedReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        mContext.registerReceiver(mNetworkConnectChangedReceiver, intentFilter);
    }

    /**
     * 设置日志
     *
     * @param islog
     */
    public void setLog(boolean islog) {
        LogUtil.setDebug(islog);
    }

    /**
     * 销毁
     */
    public void onDestory() {
        mContext.unregisterReceiver(mNetworkConnectChangedReceiver);
        mNetworkConnectChangedReceiver = null;
        mlt = null;
        TcpCmd.isConnectBeat = false;
        TcpCmd.isGroupBeat = false;
    }


    public LTApi getLtApi() {
        return ltApi;
    }

    /**
     * 获取当前类
     *
     * @return
     */
    public static LTConfigure getInstance() {
        return mlt;
    }

    public Context getContext() {
        return mContext;
    }

    /**
     * 获取网络状态监听
     *
     * @param onNetworkListener
     */
    public void setOnNetworkListener(OnNetworkListener onNetworkListener) {
        mOnNetworkListener = onNetworkListener;
    }


    private static MySQLiteOpenHelper mHelper;
    private static SQLiteDatabase db;
    private static DaoMaster mDaoMaster;
    private static DaoSession mDaoSession;

    /**
     * 设置greenDao
     */
    private static void setDatabase() {
        mHelper = new MySQLiteOpenHelper(mContext, "notes-db", null);

        // 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
        // 可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。
        // 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。
        // 所以，在正式的项目中，你还应该做一层封装，来实现数据库的安全升级。
//        mHelper = DaoMaster.DevOpenHelper(this, "notes-db", null);
        try {
            db = mHelper.getWritableDatabase();
        } catch (RuntimeException e){

        }
        // 注意：该数据库连接属于 DaoMaster，所以多个 Session 指的是相同的数据库连接。
        mDaoMaster = new DaoMaster(db);
        mDaoSession = mDaoMaster.newSession();
    }

    public DaoSession getDaoSession(){
        return mDaoSession;
    }


    public GroupInfoBean getCurrentGroup(){
        String currentId = SharedPreferenceUtil.Companion.read(Constant.currentGroupId, "");
        GroupInfoBeanDao groupInfoBeanDao = getDaoSession().getGroupInfoBeanDao();
        GroupInfoBean unique = groupInfoBeanDao.queryBuilder().where(GroupInfoBeanDao.Properties.GroupID.eq(currentId)).build().unique();
        return  unique;
    }

    public void setCurrentGroup(){

    }


}
