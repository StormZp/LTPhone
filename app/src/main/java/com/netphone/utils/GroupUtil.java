package com.netphone.utils;

import com.netphone.netsdk.bean.GroupChatMsgBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by XYSM on 2018/4/19.
 */

public class GroupUtil {
    public  static ArrayList<GroupChatMsgBean> getSortReverseList(ArrayList<GroupChatMsgBean> list) {
        Collections.sort(list, new Comparator<GroupChatMsgBean>() {
            @Override
            public int compare(GroupChatMsgBean o1, GroupChatMsgBean o2) {
                return (int) (o2.getDateTime() - o1.getDateTime());
            }
        });

        return list;
    }
}
