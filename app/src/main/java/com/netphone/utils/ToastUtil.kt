package com.netphone.utils

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.netphone.R

/**
 * Created by XYSM on 2018/3/26.
 */

class ToastUtil {
    companion object {
        var context: Context? = null

        open fun init(context: Context) {
            this.context = context;
            ToastMgr.builder.init(context)
        }

        open fun toasts( content: String) {
            ToastMgr.builder.display(content,Toast.LENGTH_SHORT)
            ToastMgr.builder.display(content,Toast.LENGTH_SHORT)
        }

        open fun toastl( content: String) {
            ToastMgr.builder.display(content,Toast.LENGTH_LONG)
//            var toasts = Toast.makeText(context, content, Toast.LENGTH_LONG)
//            toasts.setGravity(Gravity.CENTER, 0, 0)
//            toasts.show()
        }
    }


    enum class ToastMgr {
        builder;

        private var view: View? = null
        private var tv: TextView? = null
        private var toast: Toast? = null

        /**
         * 初始化Toast
         * @param context
         */
        fun init(context: Context) {
            view = LayoutInflater.from(context).inflate(R.layout.toast, null)
            tv = view!!.findViewById(R.id.text) as TextView
            toast = Toast(context)
            toast!!.setView(view)
        }

        /**
         * 显示Toast
         * @param content
         * @param duration Toast持续时间
         */
        fun display(content: CharSequence, duration: Int) {
            if (content.length != 0) {
                tv!!.setText(content)
                toast!!.setDuration(duration)
                toast!!.setGravity(Gravity.CENTER, 0, 0)
                toast!!.show()
            }
        }
    }

}
