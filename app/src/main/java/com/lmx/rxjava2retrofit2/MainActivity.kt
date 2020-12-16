package com.lmx.rxjava2retrofit2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lmx.rxjava2retrofit2.activity.ViewPagerActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initOnClick()
    }

    private fun initOnClick() {
        btnHor.setOnClickListener { ViewPagerActivity.newInstance(this, 0) }
        btnVer.setOnClickListener { ViewPagerActivity.newInstance(this, 1) }
        btnFragment.setOnClickListener { ViewPagerActivity.newInstance(this, 2) }
    }
}