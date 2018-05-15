package com.netphone.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;

import com.netphone.R;
import com.netphone.config.Constant;
import com.netphone.config.EventConfig;
import com.netphone.config.MyApp;
import com.netphone.netsdk.LTApi;
import com.netphone.netsdk.LTConfigure;
import com.netphone.netsdk.base.AppBean;
import com.netphone.netsdk.bean.BroadcastBean;
import com.netphone.netsdk.bean.FriendChatMsgBean;
import com.netphone.netsdk.bean.GroupChatMsgBean;
import com.netphone.netsdk.bean.GroupInfoBean;
import com.netphone.netsdk.bean.ImageBean;
import com.netphone.netsdk.bean.UserInfoBean;
import com.netphone.netsdk.listener.OnBroadcastListener;
import com.netphone.netsdk.listener.OnGetGroupMemberListener;
import com.netphone.netsdk.listener.OnGroupChatListener;
import com.netphone.netsdk.listener.OnGroupComeInListener;
import com.netphone.netsdk.listener.OnGroupStateListener;
import com.netphone.netsdk.listener.OnLocationListener;
import com.netphone.netsdk.listener.OnManagerListener;
import com.netphone.netsdk.listener.OnNetworkListener;
import com.netphone.netsdk.listener.OnReFreshListener;
import com.netphone.netsdk.utils.EventBusUtil;
import com.netphone.netsdk.utils.LogUtil;
import com.netphone.ui.activity.BigImageActivity;
import com.netphone.ui.activity.BroadcastSendActivity;
import com.netphone.ui.activity.FriendVoiceActivity;
import com.netphone.ui.activity.GroupChatActivity;
import com.netphone.ui.activity.LoginActivity;
import com.netphone.ui.activity.MainActivity;
import com.netphone.ui.activity.VoicePlayActivity;
import com.netphone.ui.dialog.MessageDialog;
import com.storm.developapp.tools.AppManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by XYSM on 2018/4/18.
 */

public class LTListener {
    private static LTListener mListener;


    public static LTListener newInstance() {
        if (mListener == null) {
            mListener = new LTListener();
        }
        return mListener;
    }

    public LTListener() {
        initNetListener();
    }

    private void initNetListener() {
        LTConfigure.getInstance().setOnNetworkListener(new OnNetworkListener() {
            @Override
            public void onNoNet() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, MyApp.getContext().getString(R.string.text_network)));
                LTApi.getInstance().offLine();
                EventBusUtil.sendEvent(new AppBean(EventConfig.LINE_STATE, 1));
            }

            @Override
            public void onWifiNet() {

            }

            @Override
            public void onMobileNet() {
            }

            @Override
            public void onConnectFail() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, MyApp.getContext().getString(R.string.net_connect_not)));

                LTApi.getInstance().offLine();
                EventBusUtil.sendEvent(new AppBean(EventConfig.LINE_STATE, 1));
            }

            @Override
            public void onConnectSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, null));

                LTApi.getInstance().onLine(MyApp.getContext());
                EventBusUtil.sendEvent(new AppBean(EventConfig.LINE_STATE, 0));
            }
        });
    }

    public void joinGroupListener(String id) {
        LTConfigure.getInstance().getLtApi().joinGroup(id, new OnGroupComeInListener() {
            @Override
            public void onComeInSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.COME_IN_SUCCESS, null));
            }

            @Override
            public void onComeInFail(int code, String errorMessage) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.COME_IN_FAIL, errorMessage));
            }
        }, new OnGetGroupMemberListener() {
            @Override
            public void onGetMemberFail() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GET_MEMBER_FAIL, null));
            }

            @Override
            public void onGetMemberSuccess(List<UserInfoBean> members) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GET_MEMBER_SUCCESS, members));
                Constant.groupsMemberInfo = members;
            }
        }, new OnGroupStateListener() {
            @Override
            public void onMenberExit(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_EXIT, member));
            }

            @Override
            public void onMenberJoin(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_JOIN, member));
            }

            @Override
            public void onMenberhaveMac(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MENBER_HAVE_MAC, member));
            }

            @Override
            public void onMemberRelaxedMac(UserInfoBean member) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MEMBER_RELAXE_DMAC, member));
            }

            @Override
            public void onSystemReLaxedMac() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.SYSTEM_RELAXED_MAC, null));
            }

            @Override
            public void onGrabWheatSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GRAB_WHEAT_SUCCESS, null));
            }

            @Override
            public void onGrabWheatFail(int code, String error) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.GRAB_WHEAT_FAIL, error));
            }

            @Override
            public void onRelaxedMacSuccess() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RELAXED_MAC_SUCCESS, null));
            }

            @Override
            public void onRelaxedMacFail(int code, String error) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RELAXED_MAC_FAIL, error));
            }
        }, new OnGroupChatListener() {
            @Override
            public void onReceiverListener(GroupChatMsgBean bean) {
                EventBusUtil.sendEvent(new AppBean(EventConfig.RECEIVER_WORD_GROUP, bean));
            }
        });
    }


    public void setOnManagerListener() {
        LTApi.getInstance().setOnManagerListener(new OnManagerListener() {
            @Override
            public void voice(UserInfoBean userBean) {
//                jump(FriendVoiceActivity::class.java, userBean)
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", userBean);
                bundle.putInt("state", 0);

                Intent intent = new Intent(MyApp.getContext(), FriendVoiceActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(intent);
            }

            @Override
            public void listener() {
                //VoicePlayActivity
                Bundle bundle = new Bundle();
                bundle.putInt("state", 1);

                Intent intent = new Intent(MyApp.getContext(), VoicePlayActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(intent);

            }

            @Override
            public void listenerStop() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.MANAGER_LISTENR_STOP,null));
            }

            @Override
            public void receiveListener() {
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0);

                Intent intent = new Intent(MyApp.getContext(), VoicePlayActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(intent);
            }
        });
    }

    public void setOnReFreshListener() {
        LTApi.getInstance().setOnReFreshListener(new OnReFreshListener() {
//            @Override
//            public void onReFresh(UserListBean userListBean) {
//                com.netphone.netsdk.Tool.Constant.listBean = userListBean;
//                EventBusUtil.sendEvent(new AppBean(EventConfig.REFRESH_FRIEND, null));
//            }

            @Override
            public void onReFriendsFresh(List<UserInfoBean> userListBean) {
                if (Constant.myFriendList == null) {
                    Constant.myFriendList = new ArrayList<>();
                }
//                Constant.myFriendList.clear();
                LogUtil.error("LTListener", "179\tonReFriendsFresh()\n" + userListBean.size());
                Constant.myFriendList.addAll(userListBean);
                EventBusUtil.sendEvent(new AppBean(EventConfig.REFRESH_FRIEND, null));
            }

            @Override
            public void onReGroupsFresh(List<GroupInfoBean> groupListBean) {
                if (Constant.myGroupList == null) {
                    Constant.myGroupList = new ArrayList<>();
                }
//                Constant.myGroupList.clear();
                Constant.myGroupList.addAll(groupListBean);
                EventBusUtil.sendEvent(new AppBean(EventConfig.GROUP_REFRESH, null));
            }

            @Override
            public void onFriendsReFresh(UserInfoBean bean) {
                if (Constant.myFriendList == null) {
                    Constant.myFriendList = new ArrayList<>();
                }
                for (int i = 0; i < Constant.myFriendList.size(); i++) {
                    if (Constant.myFriendList.get(i).getUserId().equals(bean.getUserId())) {
                        Constant.myFriendList.set(i, bean);
                        break;
                    }
                }
                EventBusUtil.sendEvent(new AppBean(EventConfig.REFRESH_FRIEND, null));

                if (bean.getUserId().equals(LTApi.getInstance().getCurrentInfo().getUserId())) {
                    com.netphone.netsdk.Tool.Constant.info = bean;
                    EventBusUtil.sendEvent(new AppBean(EventConfig.REFRESH_SELF, null));
                }
            }

            @Override
            public void onFriendsDel(UserInfoBean bean) {
                if (Constant.myFriendList == null) {
                    Constant.myFriendList = new ArrayList<>();
                }
                for (int i = 0; i < Constant.myFriendList.size(); i++) {
                    if (Constant.myFriendList.get(i).getUserId().equals(bean.getUserId())) {
                        Constant.myFriendList.remove(i);
                        break;
                    }
                }
                EventBusUtil.sendEvent(new AppBean(EventConfig.REFRESH_FRIEND, null));
            }

            @Override
            public void onGroupReFresh(GroupInfoBean bean) {
                if (Constant.myGroupList == null) {
                    Constant.myGroupList = new ArrayList<>();
                }
                for (int i = 0; i < Constant.myGroupList.size(); i++) {
                    if (Constant.myGroupList.get(i).getGroupID().equals(bean.getGroupID())) {
                        Constant.myGroupList.set(i, bean);
//                            Constant.myGroupList.remove(i);
                        break;
                    } else if (i == Constant.myGroupList.size() - 1) {
                        Constant.myGroupList.add(bean);
                    }
                }
                EventBusUtil.sendEvent(new AppBean(EventConfig.GROUP_REFRESH, bean));
            }

            @Override
            public void onGroupDel(GroupInfoBean bean) {
                if (Constant.myGroupList == null) {
                    for (int i = 0; i < Constant.myGroupList.size(); i++) {
                        if (Constant.myGroupList.get(i).getGroupID().equals(bean.getGroupID())) {
                            Constant.myGroupList.remove(i);
                            break;
                        }
                    }
                }
                EventBusUtil.sendEvent(new AppBean(EventConfig.GROUP_DEL, bean));

            }

            @Override
            public void onWordBroadcast(BroadcastBean msgBean) {
                Intent intent = new Intent(MyApp.getContext(), MessageDialog.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", msgBean);
                intent.putExtras(bundle);
                MyApp.getInstense().getContext().startActivity(intent);
            }

            @Override
            public void onSqueezeLine() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.SQUEEZE_OFF_LINE, null));

            }

            @Override
            public void onElectronWall() {
                showNotification(MyApp.getContext(), 100, MyApp.getContext().getResources().getString(R.string.Fences), MyApp.getContext().getResources().getString(R.string.Fences_out));
            }

            @Override
            public void onMultiMedia(ImageBean bean) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", bean);
                MyApp.getContext().startActivity(new Intent(MyApp.getContext(), BigImageActivity.class).putExtras(bundle));
            }

            @Override
            public void onFriendChatMsg(FriendChatMsgBean bean) {
                AppBean appBean = new AppBean(EventConfig.FRIEND_SEND_MSG, bean);
                appBean.setMsg(bean.getReceiveId());
                EventBusUtil.sendEvent(appBean);
            }

            @Override
            public void onBroadcastCome(int state) {
                Intent intent = new Intent(MyApp.getContext(), BroadcastSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state", state);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstense().getContext().startActivity(intent);
            }

            @Override
            public void onFriendVoice(UserInfoBean userBean) {
                Intent intent = new Intent(MyApp.getContext(), FriendVoiceActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", userBean);
                bundle.putSerializable("state", 1);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstense().getContext().startActivity(intent.putExtras(bundle));
            }

            @Override
            public void groupCome(GroupInfoBean comeBean) {
                Intent intent = new Intent(MyApp.getContext(), GroupChatActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", comeBean);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(intent);
            }

            @Override
            public void dizzy() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, MyApp.getContext().getString(R.string.been_shaken)));
                ToastUtil.Companion.toasts(MyApp.getContext().getString(R.string.been_shaken));
            }

            @Override
            public void dizzyCancel() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, null));
            }

            @Override
            public void shake() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, MyApp.getContext().getString(R.string.account_shake_off)));
                ToastUtil.Companion.toastl(MyApp.getContext().getString(R.string.account_shake_off));

                AppManager.Companion.getAppManager().finishAllActivity();
                Intent intent = new Intent(MyApp.getContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getContext().startActivity(intent);
            }

            @Override
            public void shakeCancel() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_STATE, null));
            }
        });
    }

    public void setOnBroadcastListener() {
        LTApi.getInstance().setOnBroadcastListener(new OnBroadcastListener() {
            @Override
            public void onSend() {
                Intent intent = new Intent(MyApp.getContext(), BroadcastSendActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstense().getContext().startActivity(intent);
            }

            @Override
            public void onStop() {
                EventBusUtil.sendEvent(new AppBean(EventConfig.BROADCAST_OVER, null));
            }

            @Override
            public void onReceiver() {
                Intent intent = new Intent(MyApp.getContext(), BroadcastSendActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("state", 0);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                MyApp.getInstense().getContext().startActivity(intent);
            }
        });
    }

    /**
     * 电子围栏通知栏
     *
     * @param context
     * @param id
     * @param title
     * @param text
     */
    private void showNotification(Context context, int id, String title, String text) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle(title);
        builder.setContentText(text);
        //设置为true，点击该条通知会自动删除，false时只能通过滑动来删除
        builder.setAutoCancel(true);
        builder.setOnlyAlertOnce(true);
        // 需要VIBRATE权限 设置上述铃声，振动，闪烁用|分隔，常量在Notification里
        builder.setDefaults(Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
        //设置优先级，级别高的排在前面
        builder.setPriority(Notification.PRIORITY_DEFAULT);

        // Creates an explicit intent for an Activity in your app
        //自定义打开的界面
        Intent resultIntent = new Intent(context, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(id, builder.build());
    }

    public void sendLocation(int type, OnLocationListener onLocationListener) {
        if (type == 0) {

            LTApi.getInstance().sendLocation(onLocationListener);
        } else {

            LTApi.getInstance().help(onLocationListener);
        }
    }
}
