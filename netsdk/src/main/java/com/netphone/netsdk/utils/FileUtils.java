package com.netphone.netsdk.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;

import com.netphone.netsdk.LTConfigure;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by lgp on 2017/9/4.
 * 图片文件工具类
 */

public class FileUtils {
    //大小压缩
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //质量压缩
    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > 63) {  //循环判断如果压缩后图片是否大于63kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static Bitmap comp(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        if (baos.toByteArray().length / 1024 > 1024) {//判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, 70, baos);//这里压缩30%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);
        return compressImage(bitmap);//压缩好比例大小后再进行质量压缩
    }

    //bitmap 转byte数组
    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 文件转化为字节数组
     *
     * @param file
     * @return
     */
    public static byte[] getBytesFromFile(File file) {
        byte[] ret = null;
        try {
            if (file == null) {
                // log.error("helper:the file is null!");
                return null;
            }
            FileInputStream in = new FileInputStream(file);
            ByteArrayOutputStream out = new ByteArrayOutputStream(4096);
            byte[] b = new byte[4096];
            int n;
            while ((n = in.read(b)) != -1) {
                out.write(b, 0, n);
            }
            in.close();
            out.close();
            ret = out.toByteArray();
        } catch (IOException e) {
            // log.error("helper:get bytes from file process error!");
            e.printStackTrace();
        }
        return ret;
    }

    /**
     * 写文件到SDcard
     *
     * @param file
     * @param string
     */
    public static void writeFileToSDCard(File file, String string) {
        if (isSDCardExists()) {
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(string.getBytes());

                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 判断文件及目录是否存在，若不存在则创建文件及目录
     *
     * @param filepath
     * @return
     * @throws Exception
     */
    public static File checkExist(String filepath) throws Exception {
        File file = new File(filepath);

        if (file.exists()) {//判断文件目录的存在
//            System.out.println("文件夹存在！");
            if (file.isDirectory()) {//判断文件的存在性
//                System.out.println("文件存在！");
            } else {
                file.createNewFile();//创建文件
//                System.out.println("文件不存在，创建文件成功！");
            }
        } else {
//            System.out.println("文件夹不存在！");
            File file2 = new File(file.getParent());
            file2.mkdirs();
//            System.out.println("创建文件夹成功！");
            if (file.isDirectory()) {
//                System.out.println("文件存在！");
            } else {
                file.createNewFile();//创建文件
//                System.out.println("文件不存在，创建文件成功！");
            }
        }
        return file;
    }

    /**
     * 读取文件的大小
     */

    public static long getFileSize(File f) throws Exception {

        long l = 0;

        if (f.exists()) {
            FileInputStream mFIS = new FileInputStream(f);

            l = mFIS.available();

        } else {

//            f.createNewFile();

        }

        return l;

    }


    /**
     * 判读SD卡是否存在
     *
     * @return true/false
     */
    public static boolean isSDCardExists() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    /**
     * 把字节数组保存为一个文件
     *
     * @param b
     * @param outputFile
     * @return
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        File ret = null;
        BufferedOutputStream stream = null;
        try {
            ret = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(ret, true);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
//           Log2Util.error("helper:get file from byte process error!"+e);
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
//                    Log2Util.error("helper:get file from byte process error!"+e);
                    // log.error("helper:get file from byte process error!");
                    e.printStackTrace();
                }
            }
        }
        return ret;
    }

    /**
     * 日志存储位置在6.0之前和之后不同
     *
     * @return
     */
    public static File getRootFile() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            if (Build.VERSION.SDK_INT >= 23) {
                return LTConfigure.getInstance().getContext().getExternalFilesDir("");
            } else {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    String pkName = LTConfigure.getInstance().getContext().getPackageName();
                    String path = "/sdcard/" + pkName;
                    return new File(path);
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public static String getLogFilePath() {
        return getRootFile() + "/Logs";
    }

    public static String getCrashLogFile() {
        return getRootFile() + "/Crash";
    }

    public static String getDatabaseDirectory() {
        return getRootFile() + "/Database";
    }

    public static String getLogDirectory() {
        return getRootFile() + "/Logs";
    }



    //在SD卡上创建文件
    public File createFileInSDCard(String fileName,String dir) throws IOException {
        File file=new File(dir+File.separator+fileName);
        file.createNewFile();
        return file;
    }

    //在SD卡上创建目录
    public File createSDDir(String dir)throws IOException{
        File dirFile=new File(dir);
        dirFile.mkdir();//mkdir()只能创建一层文件目录，mkdirs()可以创建多层文件目录
        return dirFile;
    }

    //判断文件是否存在
    public boolean isFileExist(String fileName,String dir){
        File file=new File(dir+File.separator+fileName);
        return file.exists();
    }

    //将一个InoutStream里面的数据写入到SD卡中
    public File write2SDFromInput(String fileName,String dir,InputStream input){
        File         file   =null;
        OutputStream output =null;
        try {
            //创建目录
            createSDDir(dir);
            //创建文件
            file=createFileInSDCard(fileName,dir);
            //写数据流
            output=new FileOutputStream(file);
            byte buffer[]=new byte[4*1024];//每次存4K
            int temp;
            //写入数据
            while((temp=input.read(buffer))!=-1){
                output.write(buffer,0,temp);
            }
            output.flush();
        } catch (Exception e) {
            System.out.println("写数据异常："+e);
        }
        finally{
            try {
                output.close();
            } catch (Exception e2) {
                System.out.println(e2);
            }
        }
        return file;
    }

}
