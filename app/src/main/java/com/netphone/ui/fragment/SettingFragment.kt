package com.netphone.ui.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.netphone.BuildConfig
import com.netphone.R
import com.netphone.databinding.FragmentSettingBinding
import com.netphone.listener.PermissionListener
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.Constant
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.ImageBean
import com.netphone.netsdk.listener.OnLocationListener
import com.netphone.netsdk.listener.OnUpFileListener
import com.netphone.ui.activity.*
import com.netphone.ui.dialog.PermissionDilog
import com.netphone.utils.GlideCircleTransform
import com.netphone.utils.GlideLoader
import com.netphone.utils.LTListener
import com.netphone.utils.PermissionUtil
import com.storm.developapp.tools.AppManager
import com.storm.tool.base.BaseFragment
import com.yancy.imageselector.ImageConfig
import com.yancy.imageselector.ImageSelector
import com.yancy.imageselector.ImageSelectorActivity


/**
 * Created by XYSM on 2018/4/16.
 */

class SettingFragment : BaseFragment<FragmentSettingBinding>() {
    val PIC_COED = 1
    val REQUEST_CAMERA = 2

    private var fragment: Fragment? = null
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initBinding(R.layout.fragment_setting, container)
        return mView
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            // Get Image Path List
            val pathList = data!!.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT)
            for (path in pathList) {
//                Log.i("ImagePathList", path)
                LTApi.newInstance().upImage(path, object : OnUpFileListener {
                    override fun upFail() {
                        activity.runOnUiThread {
                            toasts(context.getResources().getString(R.string.update_fail))
                        }
                    }

                    override fun upSuccess() {
                        activity.runOnUiThread {
                            toasts(context.getResources().getString(R.string.upload_seccess))
                        }
                    }
                });
            }
        }
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    override fun initListener() {
        binding.click = onClick()
        binding.onlineSwitch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                LTApi.newInstance().onLine(context.applicationContext)
            } else {
                LTApi.newInstance().offLine()
            }
        }
    }

    override fun initData() {
        binding.title.back.visibility = View.INVISIBLE
        binding.title.title.text = context.resources.getString(R.string.setting_content)

        fragment = this
        if (Constant.info != null) {
            binding.tvNickname.text = Constant.info.realName
//            binding.onlineState.text = if (Constant.info.isOnLine == 1) "在线1" else "离线1"
            binding.onlineSwitch.isChecked = true

            Glide.with(context).load(TcpConfig.URL + Constant.info.getHeadIcon()).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)

        }

        if (BuildConfig.DEBUG){
//            binding.lltex
        }


    }

    inner class onClick {
        open fun openUserInfo(view: View) {
            jump(UserInfoActivity::class.java)
        }

        open fun openPwChange(view: View) {
            jump(ChangePWActivity::class.java)
        }

        open fun exit(view: View) {
            AppManager.appManager.finishAllActivity()
            jump(LoginActivity::class.java)

        }

        open fun aboutApp(view: View) {
            jump(AboutAppActivity::class.java)
        }

        open fun help(view: View) {
            LTListener.newInstance().sendLocation(1, object : OnLocationListener {
                override fun onError() {
                }

                override fun onSendSuccess() {
                }

                override fun onSendFail() {
                }

                override fun onHelpSuccess() {
                    toasts(context.resources.getString(R.string.send_help_success))
                }

                override fun onHelpFail() {
                    toasts(context.resources.getString(R.string.help_fail))
                }
            })
        }

        open fun sendLocation(view: View) {
            LTListener.newInstance().sendLocation(0, object : OnLocationListener {
                override fun onError() {
                }

                override fun onSendSuccess() {
                    toasts(context.resources.getString(R.string.send_address_success))
                }

                override fun onSendFail() {
                    toasts(context.resources.getString(R.string.send_help_fail))
                }

                override fun onHelpSuccess() {

                }

                override fun onHelpFail() {

                }
            })
        }

        open fun upImage(view: View) {
            PermissionUtil.requiestPermission(activity, context, object : PermissionListener {
                override fun PermissionFail() {
                    toasts("你需要同意该权限才能使用该功能")
                }

                override fun PermissionNever() {
                }

                override fun PermissionSuccess() {
                    val imageConfig = ImageConfig.Builder(GlideLoader())
                            .steepToolBarColor(resources.getColor(R.color.blue))
                            .titleBgColor(resources.getColor(R.color.blue))
                            .titleSubmitTextColor(resources.getColor(R.color.white))
                            .titleTextColor(resources.getColor(R.color.white))
                            // 开启单选   （默认为多选）
                            .singleSelect()
                            // 开启拍照功能 （默认关闭）
                            .showCamera()
                            // 拍照后存放的图片路径（默认 /temp/picture） （会自动创建）
                            .filePath("/ImageSelector/Pictures")
                            .build()


                    ImageSelector.open(fragment, imageConfig)   // 开启图片选择器
                }
            }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        open fun upFile(view: View) {
            PermissionUtil.requiestPermission(activity, context, object : PermissionListener {
                override fun PermissionFail() {
                    toasts("你需要同意该权限才能使用该功能")
                }

                override fun PermissionNever() {
                }

                override fun PermissionSuccess() {
                }
            }, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        }

        open fun ReceiveImage(view: View) {

        }

        open fun head(view: View) {
            var bundle = Bundle()
            var imageBean = ImageBean()
            imageBean.resourceHref = Constant.info.headIcon
            bundle.putSerializable("bean", imageBean)
            jump(BigImageActivity::class.java, bundle)
        }

        open fun test(view: View) {
            jump(PermissionDilog::class.java)
        }
    }


}
