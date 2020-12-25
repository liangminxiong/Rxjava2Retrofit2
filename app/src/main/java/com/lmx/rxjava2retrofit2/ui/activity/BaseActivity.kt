package com.lmx.rxjava2retrofit2.ui.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions

/**
 * Created by lmx on 2020/12/16
 * Describe: base
 */
abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = Color.TRANSPARENT
        }
        if (layoutId() != 0) {
            setContentView(layoutId())
        }
        initView()
    }

    abstract fun layoutId(): Int

    protected abstract fun initView()

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
                    getPermissionsPass()
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

    open fun showToast(text: String) {
        Toast.makeText(this@BaseActivity, text, Toast.LENGTH_SHORT).show()
    }

    /*
    *获取权限成功
    */
    open fun getPermissionsPass() {

    }

}
