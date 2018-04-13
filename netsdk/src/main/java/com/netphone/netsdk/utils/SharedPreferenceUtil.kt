package com.netphone.netsdk.utils

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * Created by Administrator on 2017/7/26 0026.
 * 保存数据
 */

open class SharedPreferenceUtil {
    companion object {
        private lateinit var mSharedPreferences: SharedPreferences

        fun init(context: Context) {
            mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        }

        //一次性插入一个值
        fun <T> insert(key: String, value: T): Boolean {
            return put(key, value)
        }

        // 一次只读一个数据
        fun read(key: String, defValue: String): String {
            return mSharedPreferences.getString(key, defValue)
        }

        fun read(key: String, defValue: Long): Long {
            return mSharedPreferences.getLong(key, defValue)
        }

        fun read(key: String, defValue: Int): Int {
            return mSharedPreferences.getInt(key, defValue)
        }

        fun read(key: String, defValue: Boolean): Boolean {
            return mSharedPreferences.getBoolean(key, defValue)
        }

        fun read(key: String, defValue: Float): Float {
            return mSharedPreferences.getFloat(key, defValue)
        }

        fun <T> put(key: String, value: T): Boolean {
            val editor = mSharedPreferences.edit()

            if (value is String) {
                editor.putString(key, value as String)
            }

            if (value is Int) {
                editor.putInt(key, value as Int)
            }

            if (value is Boolean) {
                editor.putBoolean(key, value as Boolean)
            }
            if (value is Float) {
                editor.putFloat(key, value as Float)
            }
            if (value is Long) {
                editor.putLong(key, value as Long)
            }

            return editor.commit()

        }

        // 移除某一键值
        fun remove(key: String): Boolean {
            val editor = mSharedPreferences.edit()
            if (hasKey(key)) {
                editor.remove(key)
                return editor.commit()
            }
            return false
        }

        //清空所有的数据
        fun clear(): Boolean {
            val editor = mSharedPreferences.edit()
            editor.clear()
            return editor.commit()
        }


        //判断是否有key
        fun hasKey(key: String): Boolean {
            return mSharedPreferences.contains(key)
        }
    }


}
