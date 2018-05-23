package com.netphone.ui.dialog

import android.Manifest
import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.DialogPrermissionBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity
import com.yanzhenjie.permission.AndPermission
import com.yanzhenjie.permission.PermissionListener

/**
 * Created by XYSM on 2018/4/20.
 */

class PermissionDialog : BaseActivity<DialogPrermissionBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.dialog_prermission)
    }

    override fun initData() {
    }

    override fun initListener() {
        binding.click = OnClick()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AndPermission.onRequestPermissionsResult(requestCode, permissions, grantResults, object : PermissionListener {
            override fun onSucceed(requestCode: Int, grantPermissions: MutableList<String>?) {
                LTConfigure.getInstance().startLocationService()
                activity.finish()
            }

            override fun onFailed(requestCode: Int, deniedPermissions: MutableList<String>?) {
                // 第一种：用默认的提示语。
            }
        });
    }

    inner class OnClick {
        open fun sure(view: View) {
            AndPermission.with(activity)
                    .requestCode(100)
                    .permission(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE)
                    .send();

//            var rxPermissions = RxPermissions(activity)
//            rxPermissions.requestEach(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(Consumer<Permission> { permission ->
//                if (permission.granted) {
//                    // 用户已经同意该权限
//                    LTConfigure.getInstance().startLocationService()
//                    activity.finish()
//                } else if (permission.shouldShowRequestPermissionRationale) {
//                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
//                } else {
//                    // 用户拒绝了该权限，并且选中『不再询问』
//                }
//            })
        }

        open fun nunn(view: View) {
        }

        open fun finish(view: View) {
            activity.finish()
        }
    }


}
