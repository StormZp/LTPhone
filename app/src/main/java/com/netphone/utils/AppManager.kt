package com.storm.developapp.tools

import android.app.Activity
import android.app.ActivityManager
import android.content.Context
import android.os.Build
import com.netphone.netsdk.utils.LogUtil
import java.lang.Exception
import java.util.*

/**
 * Created by Administrator on 2017/5/25 0025.
 */

class AppManager public constructor() {

    /**
     * 添加Activity到堆中
     */
    fun addActivity(activity: Activity) {
        getActTask().add(activity)
    }

    fun removeActivity(activity: Activity){
        getActTask().remove(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后后一个压入的类
     */
    fun currentActivity(): Activity {

        return getActTask().get(activityStack!!.size-1)
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的类
     */
    fun finishActivity() {
        val activity = getActTask().get(activityStack!!.size-1)
        finishActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?) {
        if (activity != null) {
            getActTask().remove(activity)
            activity.finish()
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in getActTask()) {
            if (activity == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity() {
        try {
            if (getActTask() != null) {
                for (i in getActTask()) {
                    if (i != null && !i.isFinishing) {
                        i.finish()
                    }
                }
                getActTask().clear()
            }


        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 退出应用程
     */
    fun AppExit(context: Context) {
        try {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            activityMgr.restartPackage(context.packageName)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.CUPCAKE) {
                activityMgr.restartPackage(context.packageName)
            }
            System.exit(0)
        } catch (e: Exception) {
            LogUtil.error("AppManager.kt",e.toString())
        }
    }


    companion object {
        private var activityStack: ArrayList<Activity>? = null
        private var instance: AppManager? = null
        /**
         * 单一实例
         */
        val appManager: AppManager get() {
            if (instance == null) {
                instance = AppManager()
            }
            return instance as AppManager
        }
    }

    /**
     * 获取activity的列表
     * @return
     */
    fun getActTask(): ArrayList<Activity> {
        if (activityStack == null) {
            activityStack = ArrayList<Activity>()
        }
        return activityStack!!
    }



}
