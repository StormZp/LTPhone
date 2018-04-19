package com.netphone.netphonesdk

import com.netphone.utils.ChatTimeUtil
import org.junit.Test

/**
 * Created by XYSM on 2018/4/19.
 */

class ChatTimeUtilTest{
    @Test
    fun getInterval_text(){

//        print(interval)
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000))
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000*60))
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000*60*60))
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000*60*60*24))
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000*60*60*24*7))
        System.out.println(ChatTimeUtil.getInterval(System.currentTimeMillis()-1000*60*60*24*365))
//        LogUtil.error("ChatTimeUtilTest.kt","15\tgetInterval_text()\n"+interval);
    }
}
