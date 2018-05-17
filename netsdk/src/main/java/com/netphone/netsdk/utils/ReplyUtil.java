package com.netphone.netsdk.utils;

import com.netphone.gen.ReplyMsgBeanDao;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.bean.ReplyMsgBean;

import java.util.List;

/**
 * Created by XYSM on 2018/4/21.
 */

public class ReplyUtil {

    public static void insertMsg(String receiveId,String receiveName, String userId, String msg) {
        ReplyMsgBeanDao replyMsgBeanDao = LTConfigure.getInstance().getDaoSession().getReplyMsgBeanDao();
        ReplyMsgBean    unique          = replyMsgBeanDao.queryBuilder().where(ReplyMsgBeanDao.Properties.ReceiveID.eq(receiveId), ReplyMsgBeanDao.Properties.UserId.eq(userId)).unique();
        if (unique == null) {
            unique= new ReplyMsgBean();
            unique.setReceiveID(receiveId);
            unique.setUserId(userId);
            unique.setReceiveName(receiveName);
            unique.setLastMsg(msg);
            unique.setLastTime(System.currentTimeMillis());
            unique.setUnread(1);
            replyMsgBeanDao.insertOrReplace(unique);
        } else {
            unique.setLastMsg(msg);
            int unread = unique.getUnread();
            unique.setUnread(++unread);
            unique.setReceiveName(receiveName);
            unique.setReceiveID(receiveId);
            unique.setLastTime(System.currentTimeMillis());
            replyMsgBeanDao.insertOrReplace(unique);
        }
    }

    public static void insertMsg(String receiveId,String receiveName, String userId, String msg, boolean ifRead) {
        ReplyMsgBeanDao replyMsgBeanDao = LTConfigure.getInstance().getDaoSession().getReplyMsgBeanDao();
        ReplyMsgBean    unique          = replyMsgBeanDao.queryBuilder().where(ReplyMsgBeanDao.Properties.ReceiveID.eq(receiveId), ReplyMsgBeanDao.Properties.UserId.eq(userId)).unique();
        if (unique == null) {
            unique = new ReplyMsgBean();
            unique.setUserId(userId);
            unique.setReceiveID(receiveId);
            unique.setReceiveName(receiveName);
            unique.setLastMsg(msg);
            unique.setLastTime(System.currentTimeMillis());
            replyMsgBeanDao.insertOrReplace(unique);
        } else {
            if (ifRead) {
                unique.setUnread(0);
                unique.setLastMsg(msg);
            } else {
                int unread = unique.getUnread();
                unique.setUnread(++unread);
                unique.setLastMsg(msg);
            }
            unique.setReceiveName(receiveName);
            unique.setReceiveID(receiveId);
            unique.setLastTime(System.currentTimeMillis());
            replyMsgBeanDao.insertOrReplace(unique);
        }
    }

    public static void read(String receiveId, String userId) {
        ReplyMsgBeanDao replyMsgBeanDao = LTConfigure.getInstance().getDaoSession().getReplyMsgBeanDao();
        ReplyMsgBean    unique          = replyMsgBeanDao.queryBuilder().where(ReplyMsgBeanDao.Properties.ReceiveID.eq(receiveId), ReplyMsgBeanDao.Properties.UserId.eq(userId)).unique();
        if (unique != null) {
            unique.setUnread(0);
            unique.setReceiveID(receiveId);
            unique.setLastTime(System.currentTimeMillis());
            replyMsgBeanDao.insertOrReplace(unique);
        }
    }

    public static List<ReplyMsgBean> getList(String userId) {
        ReplyMsgBeanDao replyMsgBeanDao = LTConfigure.getInstance().getDaoSession().getReplyMsgBeanDao();
        return replyMsgBeanDao.queryBuilder().where(ReplyMsgBeanDao.Properties.UserId.eq(userId),ReplyMsgBeanDao.Properties.ReceiveID.isNotNull()).orderDesc(ReplyMsgBeanDao.Properties.LastTime).list();
    }

}
