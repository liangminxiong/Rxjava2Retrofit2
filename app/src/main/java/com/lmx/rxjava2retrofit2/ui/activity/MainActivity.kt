package com.lmx.rxjava2retrofit2.ui.activity

import android.content.Intent
import com.hjq.permissions.Permission
import com.lmx.rxjava2retrofit2.R
import com.lmx.rxjava2retrofit2.camera.CameraGlSurfaceViewActivity
import com.lmx.rxjava2retrofit2.dialog.TestDialogFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    override fun layoutId(): Int = R.layout.activity_main

    override fun initView() {
        getPermissions(Permission.CAMERA)
    }

    override fun getPermissionsPass() {
        super.getPermissionsPass()
        initOnClick()
    }

    private fun initOnClick() {
        btnHor.setOnClickListener { ViewPagerActivity.newInstance(this, 0) }
        btnVer.setOnClickListener { ViewPagerActivity.newInstance(this, 1) }
        btnFragment.setOnClickListener { ViewPagerActivity.newInstance(this, 2) }
        btnDialog.setOnClickListener {
            TestDialogFragment().showNow(
                supportFragmentManager,
                "test" + System.currentTimeMillis()
            )
        }
        btnFriends.setOnClickListener { FriendCircleActivity.newInstance(this, 0) }
        btnCamera.setOnClickListener {
            startActivity(
                Intent().setClass(
                    this,
                    CameraGlSurfaceViewActivity::class.java
                )
            )
        }
    }
}