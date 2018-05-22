package com.netphone.ui.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.bigkoo.alertview.AlertView
import com.bigkoo.alertview.OnItemClickListener
import com.bumptech.glide.Glide
import com.netphone.R
import com.netphone.databinding.ActivityUserInfoBinding
import com.netphone.listener.PermissionListener
import com.netphone.netsdk.LTApi
import com.netphone.netsdk.Tool.TcpConfig
import com.netphone.netsdk.base.AppBean
import com.netphone.netsdk.bean.UserInfoBean
import com.netphone.netsdk.listener.OnChangeUserInfoListener
import com.netphone.netsdk.listener.OnUpFileListener
import com.netphone.netsdk.utils.LogUtil
import com.netphone.utils.GlideCircleTransform
import com.netphone.utils.GlideLoader
import com.netphone.utils.PermissionUtil
import com.storm.tool.base.BaseActivity
import com.yancy.imageselector.ImageConfig
import com.yancy.imageselector.ImageSelector
import com.yancy.imageselector.ImageSelectorActivity

/**
 * Created by XYSM on 2018/4/16.
 */
open class UserInfoActivity : BaseActivity<ActivityUserInfoBinding>() {

    private var head = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding(R.layout.activity_user_info)
    }

    private var headPath = ""
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ImageSelector.IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            // Get Image Path List
            val pathList = data!!.getStringArrayListExtra(ImageSelectorActivity.EXTRA_RESULT)
            for (path in pathList) {
//                Log.i("ImagePathList", path)
                LTApi.getInstance().upImage(path, object : OnUpFileListener {
                    override fun upFail() {
                        activity.runOnUiThread {
                            toasts(context.getResources().getString(R.string.update_fail))
                        }
                    }

                    override fun upSuccess(path: String?) {
                        if (!TextUtils.isEmpty(path)) {
                            activity.runOnUiThread {
                                //                            toasts(context.getResources().getString(R.string.upload_seccess))
                                LogUtil.error("UserInfoActivity.kt", "59\tupSuccess()\n" + path);
                                Glide.with(context).load(TcpConfig.URL + path!!).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)

                            }
                            headPath = path!!;
                        }
                    }
                });
            }
        }
    }

    override fun initData() {
        binding.title.title.text = context.resources.getString(R.string.user_info)

        var currentInfo = LTApi.getInstance().currentInfo

        if (currentInfo != null && !TextUtils.isEmpty(currentInfo.realName)) {
            binding.account.setText(currentInfo.realName)
            binding.sex.setText(currentInfo.gender)

            Glide.with(context).load(TcpConfig.URL + currentInfo!!.headIcon).placeholder(R.mipmap.icon_defult_detail).error(R.mipmap.icon_defult_detail).transform(GlideCircleTransform(context)).into(binding.ivHead)


            if (currentInfo.getGender() == null || currentInfo.getGender().equals("0"))
                binding.sex.setText(getApplicationContext().getResources().getString(R.string.woman));
            else
                binding.sex.setText(getApplicationContext().getResources().getString(R.string.man));

            binding.content.setText(currentInfo.description)
        }
    }

    override fun initListener() {
        binding.title.back.setOnClickListener { finish() }
        binding.click = OnClick()
    }

    override fun receiveEvent(appBean: AppBean<Any>) {
    }

    override fun receiveStickyEvent(appBean: AppBean<Any>) {
    }

    inner class OnClick {
        open fun back(view: View) {

        }

        open fun changeHead(view: View) {
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

                    ImageSelector.open(activity, imageConfig)   // 开启图片选择器
                }
            }, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }

        open fun sex(view: View) {
            AlertView(context.resources.getString(R.string.warn), context.resources.getString(R.string.choose_sex), context.resources.getString(R.string.text_cancel),
                    arrayOf(context.resources.getString(R.string.woman), context.resources.getString(R.string.man)), null, context, AlertView.Style.ActionSheet, OnItemClickListener { o, position ->
                if (position == 0) {
                    binding.sex.text = context.resources.getString(R.string.woman)
                } else if (position == 1) {
                    binding.sex.text = context.resources.getString(R.string.man)
                }
            }).show()
        }

        open fun change(view: View) {
            var userInfoBean = UserInfoBean()
            var username = binding.account.text.toString()
            if (TextUtils.isEmpty(username)) {
                toasts(context.getResources().getString(R.string.account) + context.getResources().getString(R.string.not_null));

                return
            }
            userInfoBean.realName = username
            if (!TextUtils.isEmpty(head)) {
                userInfoBean.headIcon = head
            }
            userInfoBean.gender = if (binding.sex.text.toString().equals("女")) "0" else "1"
            userInfoBean.description = binding.content.text.toString()
            if (!TextUtils.isEmpty(headPath)) {
                userInfoBean.headIcon = headPath
            }
            LTApi.getInstance().changeUserInfo(userInfoBean, object : OnChangeUserInfoListener {
                override fun onSuccess() {
                    activity.runOnUiThread {
                        toasts(context.getResources().getString(R.string.update_success));
                        finish()
                    }
                }

                override fun onFail() {
                    activity.runOnUiThread {
                        toasts(context.getResources().getString(R.string.update_fail));
                        finish()
                    }
                }
            })

        }
    }

}