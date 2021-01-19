package com.lmx.baselibrary.mvvm.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import com.hjq.permissions.OnPermissionCallback

import com.hjq.permissions.XXPermissions
import com.lmx.baselibrary.R
import com.lmx.baselibrary.mvvm.dialog.LoadingDialog
import com.lmx.common_mvvm.BaseViewModel
import com.lmx.common_mvvm.BaseVmDbFragment

import com.lmx.baselibrary.mvvm.AppViewModel
import com.lmx.baselibrary.mvvm.ext.getAppViewModel

/**
 * Created by lmx on 2020/5/7
 * Describe 你项目中的Fragment基类，在这里实现显示弹窗，吐司，还有自己的需求操作 ，如果不想用Databind，请继承
 * BaseVmFragment例如
 * abstract class BaseFragment<VM : BaseViewModel> : BaseVmFragment<VM>() {
 */
abstract class BaseMvvmFragment<VM : BaseViewModel, DB : ViewDataBinding> :
    BaseVmDbFragment<VM, DB>() {

    private var mLoadingDialog: LoadingDialog? = null

    val appViewModel: AppViewModel by lazy { getAppViewModel() }

    /**
     * 当前Fragment绑定的视图布局
     */
    abstract override fun layoutId(): Int


    abstract override fun initView(savedInstanceState: Bundle?)

    /**
     * 懒加载 只有当前fragment视图显示时才会触发该方法
     */
    abstract override fun lazyLoadData()

    /**
     * 创建观察者 懒加载之后才会触发
     */
    abstract override fun createObserver()


    /**
     * Fragment执行onViewCreated后触发的方法
     */
    override fun initData() {

    }


    /**
     * 加载框
     */
    override fun showLoading(message: String) {
        if (mLoadingDialog == null) {
            mLoadingDialog = activity?.let {
                LoadingDialog(it,true)
            }
        }
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.show()
        }
    }

    /**
     * 点击不取消等待框
     */
    open fun showLoading(isCancel: Boolean) {
        if (mLoadingDialog == null) {
            mLoadingDialog = activity?.let {
                LoadingDialog(it,false)
            }
        }
        if (mLoadingDialog != null && !mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.setCancelable(isCancel)
            mLoadingDialog!!.show()
        }
    }

    /**
     * 加载进度条
     */
    open fun showLoading() {
        showLoading(resources.getString(R.string.loading))
    }

    /**
     * 关闭等待框
     */
    override fun dismissLoading() {
        if (mLoadingDialog != null && mLoadingDialog!!.isShowing) {
            mLoadingDialog!!.dismiss()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun startActivity(intent: Intent?) {
        super.startActivity(intent)
        rightToLeftAnimacion()
    }

    open fun rightToLeftAnimacion() {
        if (activity != null) {
            activity!!.overridePendingTransition(
                R.anim.activity_right_to_left_enter,
                R.anim.activity_right_to_left_exit
            )
        }
    }

    open fun leftToRightAnimacion() {
        if (activity != null) {
            activity!!.overridePendingTransition(
                R.anim.activity_left_to_right_enter,
                R.anim.activity_left_to_right_exit
            )
        }
    }


    /**
     * 从下往上动画
     */
    open fun bottomToTopAnimacion() {
        if (activity != null) {
            activity!!.overridePendingTransition(R.anim.bottom_start, R.anim.bottom_start)
        }
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
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
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

}