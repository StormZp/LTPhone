package com.netphone.ui.dialog

import android.Manifest
import android.os.Bundle
import android.view.View
import com.netphone.R
import com.netphone.databinding.DialogPrermissionBinding
import com.netphone.netsdk.LTConfigure
import com.netphone.netsdk.base.AppBean
import com.storm.tool.base.BaseActivity
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.functions.Consumer

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

    inner class OnClick {
        open fun sure(view: View) {
            var rxPermissions = RxPermissions(activity)
            rxPermissions.requestEach(Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(Consumer<Permission> { permission ->
                if (permission.granted) {
                    // 用户已经同意该权限
                    LTConfigure.getInstance().startLocationService()
                    activity.finish()
                } else if (permission.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                } else {
                    // 用户拒绝了该权限，并且选中『不再询问』
                }
            })
        }

        open fun nunn(view: View) {
        }

        open fun finish(view: View) {
            activity.finish()
        }
    }


}
