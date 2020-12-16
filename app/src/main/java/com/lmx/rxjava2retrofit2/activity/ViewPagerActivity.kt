package com.lmx.rxjava2retrofit2.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.lmx.rxjava2retrofit2.R
import com.lmx.rxjava2retrofit2.adapter.ViewPagerAdapter
import com.lmx.rxjava2retrofit2.adapter.ViewPagerFragmentStateAdapter
import com.lmx.rxjava2retrofit2.fragment.PageFragment
import kotlinx.android.synthetic.main.activity_viewpager.*


/**
 * Created by lmx on 2020/12/16
 * Describe:
 */
class ViewPagerActivity : AppCompatActivity() {

    companion object {

        private const val KEY_TYPE = "type"
        fun newInstance(context: AppCompatActivity, type: Int) {
            val intent = Intent()
            intent.apply {
                setClass(context, ViewPagerActivity::class.java)
                putExtra(KEY_TYPE, type)
            }
            context.startActivity(intent)
        }
    }

    private val type: Int by lazy { intent.getIntExtra(KEY_TYPE, -1) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_viewpager)
        initViewPager()
    }

    private fun initViewPager() {
        if (type < 2) {
            viewPager.adapter = ViewPagerAdapter()
            if (type == 1) {
                viewPager.orientation = ViewPager2.ORIENTATION_VERTICAL
            } else {
                viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
            }
        } else {
            tabLayout.visibility = View.VISIBLE
            initFragment()
        }
        viewPager.overScrollMode = ViewPager2.OVER_SCROLL_NEVER
    }

    private fun initFragment() {
        val fragments = mutableListOf<Fragment>()
        for (i in 0 until 5) {
            tabLayout.addTab(tabLayout.newTab().setText("Tab$i"))
            PageFragment.newInstance(i)?.let { fragments.add(it) }
        }
        viewPager.adapter = ViewPagerFragmentStateAdapter(this, fragments)
        // 添加页签选中监听

        // 添加页签选中监听
        tabLayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewPager.currentItem = tab.position
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {}
            override fun onTabReselected(tab: TabLayout.Tab) {}
        })
        // 注册页面变化的回调接口
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                tabLayout.setScrollPosition(position, 0f, false)
            }
        })
    }
}