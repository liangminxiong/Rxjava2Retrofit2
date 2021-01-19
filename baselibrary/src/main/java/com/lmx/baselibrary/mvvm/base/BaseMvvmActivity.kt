package com.lmx.baselibrary.mvvm.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.lmx.baselibrary.ActivityStackManager
import com.lmx.baselibrary.R
import com.lmx.baselibrary.mvvm.dialog.LoadingDialog
import com.lmx.common_mvvm.BaseViewModel
import com.lmx.common_mvvm.BaseVmDbActivity
import com.lmx.baselibrary.mvvm.AppViewModel
import com.lmx.baselibrary.mvvm.ext.getAppViewModel
import me.jessyan.autosize.AutoSizeCompat
import org.greenrobot.eventbus.EventBus

/**
 * Created by lmx on 2020/5/7
 * Describe 你项目中的Activity基类，在这里实现显示弹窗，吐司，还有加入自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmActivity例如
 * abstract class BaseActivity<VM : BaseViewModel> : BaseVmActivity<VM>() {
 */
abstract class BaseMvvmActivity<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseVmDbActivity<VM, DB>() {

    private var mLoadingDialog: LoadingDialog? = null
    private var freezeDialog: Dialog? = null
    private var riskAccountDialog: Dialog? = null

    val appViewModel: AppViewModel by lazy { getAppViewModel() }


    abstract override fun layoutId(): Int

    override fun initUI(savedInstanceState: Bundle?) {
        ActivityStackManager.getInstance().onCreated(activity = this, tag = "mvvm")
        initView(savedInstanceState)
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this)
        }
    }

    abstract fun initView(savedInstanceState: Bundle?)

    /**
     * 创建观察者
     */
    abstract override fun createObserver()

    /**
     * 加载进度条
     */
    override fun showLoading(message: String) {
        if (mLoadingDialog == null && !isFinishing) {
            mLoadingDialog = LoadingDialog(
                this, true
            )
        }
        if (mLoadingDialog != null) {
            if (!mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.show()
            }
        }
    }

    /**
     * 点击不取消等待框
     */
    open fun showLoading(isCancel: Boolean) {
        if (mLoadingDialog == null && !isFinishing) {
            mLoadingDialog = LoadingDialog(this, isCancel)
        }
        if (mLoadingDialog != null) {
            if (!mLoadingDialog!!.isShowing) {
                mLoadingDialog!!.setCancelable(isCancel)
                mLoadingDialog!!.show()
            }
        }
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
            mLoadingDialog = null
        }
    }

    /**
     * 在任何情况下本来适配正常的布局突然出现适配失效，适配异常等问题，只要重写 Activity 的 getResources() 方法
     */
    override fun getResources(): Resources {
        AutoSizeCompat.autoConvertDensityOfGlobal(super.getResources())
        val res = super.getResources()
        if (res.configuration.fontScale != 1f) { //非默认值
            val newConfig = Configuration()
            newConfig.setToDefaults() //设置默认
            res.updateConfiguration(newConfig, res.displayMetrics)
        }
        return res
    }

    open fun dimissFreezeDialog() {
        if (freezeDialog != null && freezeDialog!!.isShowing) {
            freezeDialog!!.dismiss()
            freezeDialog = null
        }
    }

    /**
     * 异常退出登录回调
     */
    open fun logoutBack(msg: String?) {

        leftToRightAnimacion()
    }

    /**
     * 过渡动画
     */
    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        rightToLeftAnimacion()
    }


    open fun startActivityNoAnimation(intent: Intent?) {
        super.startActivity(intent)
    }

    override fun finish() {
        super.finish()
        leftToRightAnimacion()
    }

    open fun finishNoAnimation() {
        super.finish()
    }

    open fun finishFade() {
        super.finish()
    }

    open fun rightToLeftAnimacion() {
        overridePendingTransition(
            R.anim.activity_right_to_left_enter,
            R.anim.activity_right_to_left_exit
        )
    }

    open fun leftToRightAnimacion() {
        overridePendingTransition(
            R.anim.activity_left_to_right_enter,
            R.anim.activity_left_to_right_exit
        )
    }


    /**
     * 从下往上动画
     */
    open fun bottomToTopAnimacion() {
        overridePendingTransition(R.anim.bottom_start, R.anim.bottom_start)
    }

    /**
     * 从下往上动画
     */
    open fun finishTopToBottom() {
        super.finish()
        overridePendingTransition(R.anim.bottom_end, R.anim.bottom_end)
    }

    override fun onDestroy() {
        super.onDestroy()
        dismissLoading()
    }

    /**
     * 设置权限组
     */
    open fun getPermissions(vararg permissions: String?) {
        val xxPermissions = XXPermissions.with(this)
        //.permission(Permission.REQUEST_INSTALL_PACKAGES)
        // 申请悬浮窗权限
        //.permission(Permission.SYSTEM_ALERT_WINDOW)
        // 申请通知栏权限
        //.permission(Permission.NOTIFICATION_SERVICE)
        // 申请系统设置权限
        //.permission(Permission.WRITE_SETTINGS)
        // 申请单个权限
        xxPermissions.permission(permissions) // 申请多个权限
        requstPermissions(xxPermissions)
    }

    private fun requstPermissions(xxPermissions: XXPermissions) {
        xxPermissions.request(object : OnPermissionCallback {
            @SuppressLint("ShowToast")
            override fun onGranted(permissions: List<String>, all: Boolean) {
                if (all) {
                    hasAllPermissions()
                } else {
                    showToast("获取部分权限成功，但部分权限未正常授予")
                }
            }

            override fun onDenied(permissions: List<String>, never: Boolean) {
                if (never) {
//                            toast("被永久拒绝授权，请手动授予录音和日历权限");
                    // 如果是被永久拒绝就跳转到应用权限系统设置页面
//                        XXPermissions.startPermissionActivity(
//                            this@BaseActivity,
//                            permissions
//                        )
                    showToast("被永久拒绝授权，请手动授予权限")
                } else {
                    showToast("获取权限失败，权限未正常授予")
                }
            }
        })
    }

    /**
     * 吐司
     */
    open fun showToast(msg: String?) {
        Toast.makeText(this@BaseMvvmActivity, msg, Toast.LENGTH_SHORT).show()
    }

    /**
     * 设置权限组
     */
    fun permission(permissions: MutableList<String?>) {
        val xxPermissions = XXPermissions.with(this)
        //.permission(Permission.REQUEST_INSTALL_PACKAGES)
        // 申请悬浮窗权限
        //.permission(Permission.SYSTEM_ALERT_WINDOW)
        // 申请通知栏权限
        //.permission(Permission.NOTIFICATION_SERVICE)
        // 申请系统设置权限
        //.permission(Permission.WRITE_SETTINGS)
        // 申请单个权限
        xxPermissions.permission(permissions) // 申请多个权限
        requstPermissions(xxPermissions)
    }

    /**是否拥有全部权限*/
    fun hasAllPermissions() {

    }


    //设置字体为默认大小，不随系统字体大小改而改变
    override fun onConfigurationChanged(newConfig: Configuration) {
        if (newConfig.fontScale != 1f) //非默认值
            resources
        super.onConfigurationChanged(newConfig)
    }

}