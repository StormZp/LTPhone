package com.netphone.netsdk.Tool;

/**
 * Created by lgp on 2017/9/6.
 * tcp基本配置类
 */

public class TcpConfig {
//    public static final String HOST = "120.77.216.2";//服务器
//        public static final String HOST = "183.3.210.168";//外网
        public static final String HOST = "172.16.1.185";//本地

    public static final String URL = "http://"+HOST;//外网
    public static final int PORT = 2012;//立同
//    public static final int PORT = 3012;//菲律宾
//            public static final int PORT = 4012;//服务器
    public static final int lOCAL_PORT = 2918;//本地端口
    public static final String charsetName = "UTF-8";//默认编码
    public static final int connTimeout = 10000;//连接超时时间：10秒
    public static final int MAX_SIZE = 1024 * 10;//一次读入最大数据量
    public static final long ONE_MIN = 60 * 1000;//倒计时一分钟
}
