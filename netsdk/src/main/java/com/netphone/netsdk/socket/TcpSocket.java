package com.netphone.netsdk.socket;

import android.content.Context;

import com.google.common.primitives.Bytes;
import com.google.gson.Gson;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.Tool.TcpCmd;
import com.netphone.netsdk.Tool.TcpConfig;
import com.netphone.netsdk.utils.ByteUtil;
import com.netphone.netsdk.utils.DataTypeChangeHelper;
import com.netphone.netsdk.utils.LogUtil;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by XYSM on 2018/4/12.
 */

public class TcpSocket {
    private static TcpSocket      instance;//单例模式，因为只会在android主线程调用，不存在线程安全问题。
    private static Context        mContext;
    private        Socket         mSocket;//socket通信
    private        DatagramSocket client;//UDP客户端


    private boolean connectThreadStart;//socket连接线程
    private boolean receiveThreadStart;//接收socket数据线程

    private List<Byte>                  list             = new ArrayList<>();//用于处理拆包粘包
    private BufferedOutputStream        sendSocketStream = null;//数据输出流，可以处理各种基本数据类型，这里处理byte
    private LinkedBlockingQueue<byte[]> msgQueue         = new LinkedBlockingQueue<>();//发送的消息队列

    private TcpCmd mCmd;


    public static TcpSocket getInstance() {
        if (instance == null) {
            instance = new TcpSocket();
        }
        return instance;
    }

    public static void init(Context context) {
        mContext = context;
        if (instance == null) {
            instance = new TcpSocket();
        }
    }

    public Socket getSocket() {
        if (mSocket == null) {
            try {
                mSocket = new Socket(TcpConfig.HOST, TcpConfig.PORT);
            } catch (IOException e) {
                connectThreadStart = false;
                LogUtil.error("创建连接失败:" + e);
//                e.printStackTrace();
                return null;
            }
        }
        if (mCmd == null) {
            mCmd = new TcpCmd();
        }
        return mSocket;
    }

    public boolean isConnected() {
        if (mSocket == null || mSocket.isClosed()) {
            return false;
        }
        return mSocket.isConnected();
    }

    public synchronized void connect() {
        // connect连接
        LogUtil.error("TcpSocket", "62\nconnect()" + "connect连接");
        new Thread(connectionThread).start();
    }

    /**
     * 连接线程
     */
    Runnable connectionThread = new Runnable() {
        @Override
        public void run() {
            LogUtil.error("tcp连接线程启动了");
            connectThreadStart = true;
            try {
                if (mSocket == null || mSocket.isClosed()) {
                    LogUtil.error("TcpSocket", "91\nrun()\t" + "jinlaile");
                    mSocket = new Socket(TcpConfig.HOST, TcpConfig.PORT);
                    mSocket.setKeepAlive(true);
                }
//                LogUtil.error("创建连接成功,HOST=" + TcpConfig.HOST + "PORT=" + TcpConfig.PORT + ",localport=" + TcpConfig.lOCAL_PORT);
            } catch (Exception e) {
                connectThreadStart = false;
                LogUtil.error("创建连接失败:" + e);
                LTConfigure.getInstance().mOnNetworkListener.onConnectFail();
                // 通知
//                EventBusUtil.sendEvent(new AppBean(EventConfig.TCP_CONNECT_STATUS_FAIL, 0, null, null));
                return;
            }
            onConnectSuccess();
            connectThreadStart = false;
        }
    };

    public DatagramSocket getClient() {
        if (client == null || client.isClosed())
            try {
                client = new DatagramSocket(getSocket().getLocalPort());//新建一个DatagramSocket
                LogUtil.error("TcpSocket", "116\tgetClient()\n" + getSocket().getLocalPort());
            } catch (Exception e) {
                e.printStackTrace();
            }
        return client;
    }

    public void setClient() {
        LogUtil.error("TcpSocket", "123\tsetClient()\n" + "更新本地端口");
        //更新tcp本地端口
        if (client != null) {
            client.close();
            client = null;
            try {
                client = new DatagramSocket(getSocket().getLocalPort());//新建一个DatagramSocket,获取客户端本地端口号
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
    }

    private void onConnectSuccess() {
        LogUtil.error("tcp connect 建立成功,开始启动接发线程");
        if (LTConfigure.getInstance().mOnNetworkListener != null)
            LTConfigure.getInstance().mOnNetworkListener.onConnectSuccess();

        new Thread(sendThread).start();
        new Thread(receiveThread).start();
    }

    /**
     * 发送线程
     */
    private Runnable sendThread = new Runnable() {

        @Override
        public synchronized void run() {
            LogUtil.error("发送线程启动了");
            try {
                byte[] datas;
                while (mSocket != null && mSocket.isConnected()) {

                    datas = msgQueue.take();
                    if (datas != null && mSocket != null && mSocket.isConnected()) {
                        sendSocketStream = new BufferedOutputStream(mSocket.getOutputStream());
                        LogUtil.error("发送队列里面的消息:" + new Gson().toJson(datas));
                        sendSocketStream.write(datas);
                        sendSocketStream.flush();
                    }
                }
            } catch (Exception e) {
                LogUtil.error("发送线程", e);
                disconnect();
                connect();
                e.printStackTrace();
            }
            LogUtil.error("发送线程结束了");
        }
    };

    /**
     * 接收线程
     */
    Runnable receiveThread = new Runnable() {
        @Override
        public void run() {
            LogUtil.error("接受线程启动了");
            receiveThreadStart = true;
            try {
                byte[]          cmdOrvoice;
                byte[]          realBytes;
                DataInputStream inputStream;
                while (receiveThreadStart) {
                    //先进行判断是指令数据还是语音数据
                    cmdOrvoice = new byte[TcpConfig.MAX_SIZE];
                    if (!receiveThreadStart) {
                        break;
                    }
                    inputStream = new DataInputStream(mSocket.getInputStream());
                    int red = 0;
                    try {
                        red = inputStream.read(cmdOrvoice, 0, TcpConfig.MAX_SIZE);
                    } catch (SocketException es) {
                        break;
                    }


                    if (red > 0) {
                        realBytes = new byte[red];
                        System.arraycopy(cmdOrvoice, 0, realBytes, 0, red);
//                        Log2Util.error("realBytes的长度" + new Gson().toJson(realBytes));
                        // 接收数据
                        handleData(realBytes);
                    }
                }
            } catch (Exception e) {
                receiveThreadStart = false;
                LogUtil.error("onErrorDisConnect ", e);
                disconnect();
                connect();
            }
//            receiveThreadStart = false;
            LogUtil.error("接受线程结束了");
        }
    };

    public synchronized void disconnect() {
        receiveThreadStart = false;
        try {
            if (mSocket != null)
                mSocket.close();
            mSocket = null;
            sendSocketStream.close();
            sendSocketStream = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 将指令反馈给服务类以便进行UI的交互等。
     *
     * @param pagBytes
     */
    private void handleCmd(byte[] pagBytes) {
        if (mCmd == null) {
            mCmd = new TcpCmd();
        }
        mCmd.cmdData(pagBytes);
    }

    private synchronized void handleData(final byte[] data) {
        try {
            LogUtil.error("TcpSocket", "251\thandleData()\n" + "原始数据长度:" + data.length + "\n" + new Gson().toJson(data));
            List<Byte> tempLists = Bytes.asList(data);
            list.addAll(tempLists);
        } catch (Exception e) {
            LogUtil.error(e.getMessage());
            e.getMessage();
        }
        int next = 0;
        if (list.size() == 0)
            return;
        if (list.get(0) == (byte) 0x7E) {
            //指令处理
            if (list.size() < 11)
                return;//不够长度，等待下一次数据
            byte[] cmdbytes = new byte[2];//求指令包内容长度
            cmdbytes[0] = list.get(9);
            cmdbytes[1] = list.get(10);
            int nextContent = DataTypeChangeHelper.byte2int(cmdbytes);
            next = nextContent + 14;
            if (next >= 14) {//可能有无回复内容这种情况，所以考虑=号
                while (list.size() >= next) {
                    byte[] pack  = new byte[next];
                    byte[] bytes = Bytes.toArray(list);
                    System.arraycopy(bytes, 0, pack, 0, next);
                    list = list.subList(next, list.size());
//                    Log2Util.error(TAG, "处理后剩余数据" + new Gson().toJson(list));
                    handleCmd(pack);
                    if (list.size() < 4)
                        break;//剩余长度不够就跳出循环,以最短的语音包固定长度为准
                    if (list.get(0) == (byte) 0x7E) {
                        if (list.size() < 14)
                            break;//还是指令包的话，且长度小于14就跳出循环
                        //否则，求下一个指令包长度
                        byte[] ab = new byte[2];
                        ab[0] = list.get(9);
                        ab[1] = list.get(10);
                        int nextContentCmds = DataTypeChangeHelper.byte2int(ab);
                        next = nextContentCmds + 14;
                    } else {
                        //不能处理，数据清空
                        list.clear();
                        break;
                    }
                }
            } else {
                //不能处理，数据清空
                list.clear();
            }
        } else {
            //不能处理，数据清空
            list.clear();
        }
    }


    //添加发送数据
    public void addData(byte[] datas) {
        if (!isConnected()){
            getSocket();
        }

        LogUtil.error("添加发送数据" + new Gson().toJson(datas
        ));
        if (datas != null && mSocket != null)
            addLinkQueue(datas);
    }

    //先放进来，线程安全，这样一个个取传出去再发送
    public void addLinkQueue(byte[] datas) {
        try {
            if (msgQueue != null && datas != null) {
                Object[] objects = ByteUtil.splitAry(datas, 1000);
                for (Object obj : objects) {
                    byte[] bytedata = (byte[]) obj;
                    msgQueue.put(bytedata);
                }
            }
        } catch (InterruptedException e) {
            LogUtil.error(e.getMessage());
            e.printStackTrace();
        }
    }

}
