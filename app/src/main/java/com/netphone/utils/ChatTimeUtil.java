package com.netphone.utils;

import org.jetbrains.annotations.Contract;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by XYSM on 2018/4/19.
 */

public class ChatTimeUtil {
    /**

     * 获取时间间隔

     * @param inputTime 传入的时间格式必须类似于“yyyy-MM-dd HH:mm:ss”这样的格式

     * @return

     **/
    @Contract("!null -> !null")
    public static String getInterval(Long inputTime) {

        if(inputTime ==null){

            return "";
        }

        String result= null;

        try {

//            SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            ParsePosition pos = new ParsePosition(0);

            Date d1 = new Date(inputTime);

            // 用现在距离1970年的时间间隔new

            // Date().getTime()减去以前的时间距离1970年的时间间隔d1.getTime()得出的就是以前的时间与现在时间的时间间隔

            long time = new Date().getTime() - d1.getTime();// 得出的时间间隔是毫秒

            if (time / 1000 <= 0 ) {

                // 如果时间间隔小于等于0秒则显示“刚刚”time/10得出的时间间隔的单位是秒

                result= "刚刚";

            } else if (time / 1000 < 60 ) {

                // 如果时间间隔小于60秒则显示多少秒前

                int se = (int) ((time % 60000) / 1000);

                result= se + "秒前";

            } else if (time / 60000 < 60 ) {

                // 如果时间间隔小于60分钟则显示多少分钟前

                int m = (int) ((time % 3600000) / 60000);// 得出的时间间隔的单位是分钟

                result= m + "分钟前";

            } else if (time / 3600000 < 24 ) {

                // 如果时间间隔小于24小时则显示多少小时前

                int h = (int) (time / 3600000);// 得出的时间间隔的单位是小时

                result= h + "小时前";

            }else if (time / 86400000 < 2 ) {

                // 如果时间间隔小于2天则显示昨天

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                result= sdf.format(d1.getTime());

                result= "昨天" +
                        result;

            }else if (time / 86400000 < 3 ) {

                // 如果时间间隔小于3天则显示前天

                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");

                result= sdf.format(d1.getTime());

                result= "前天" +
                        result;

            }else if (time / 86400000 < 30 ) {

                // 如果时间间隔小于30天则显示多少天前

                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");

                result= sdf.format(d1.getTime());

            } else if (time / 2592000000l < 12 ) {

                // 如果时间间隔小于12个月则显示多少月前

                SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");

                result= sdf.format(d1.getTime());



            }else {

//                // 大于1年，显示年月日时间

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");

                result= sdf.format(d1.getTime());

            }

        } catch (Exception e) {

            return "";

        }

        return result;

    }
}
