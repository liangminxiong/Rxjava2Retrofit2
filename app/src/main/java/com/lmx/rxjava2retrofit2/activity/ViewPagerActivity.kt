package com.lmx.rxjava2retrofit2.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.OvershootInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.lmx.magicindicator.FragmentContainerHelper
import com.lmx.magicindicator.MagicIndicator
import com.lmx.magicindicator.UIUtil
import com.lmx.magicindicator.abs.IPagerIndicator
import com.lmx.magicindicator.abs.IPagerTitleView
import com.lmx.magicindicator.buildins.CommonNavigatorAdapter
import com.lmx.magicindicator.buildins.commonnavigator.CommonNavigator
import com.lmx.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator
import com.lmx.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView
import com.lmx.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView
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
            llIndicator.visibility = View.VISIBLE
            initFragment()
        }
        viewPager.overScrollMode = ViewPager2.OVER_SCROLL_NEVER
    }

    private fun initFragment() {
        val fragments = mutableListOf<Fragment>()
        for (i in 1 until 12) {
            PageFragment.newInstance(i)?.let { fragments.add(it) }
        }
        viewPager.adapter = ViewPagerFragmentStateAdapter(this, fragments)
        val commonNavigator = CommonNavigator(this)
        commonNavigator.setAdapter(object : CommonNavigatorAdapter() {
            override fun getCount(): Int = fragments.size

            @SuppressLint("SetTextI18n")
            override fun getTitleView(context: Context?, index: Int): IPagerTitleView? {
                val simplePagerTitleView: SimplePagerTitleView =
                    ColorTransitionPagerTitleView(context!!)
                simplePagerTitleView.setNormalColor(Color.GRAY)
                simplePagerTitleView.setSelectedColor(Color.RED)
                simplePagerTitleView.text = "Fragment $index"
                simplePagerTitleView.setOnClickListener {
                    viewPager.currentItem = index
                }
                return simplePagerTitleView
            }

            override fun getIndicator(context: Context?): IPagerIndicator? {
                val linePagerIndicator = LinePagerIndicator(context!!)
                linePagerIndicator.setMode(LinePagerIndicator.MODE_EXACTLY)
                linePagerIndicator.setLineWidth(UIUtil.dip2px(context, 20.0).toFloat())
                linePagerIndicator.setColors(Color.RED)
                return linePagerIndicator
            }
        })

        val magicIndicator = MagicIndicator(this)
        llIndicator.addView(magicIndicator)
        magicIndicator.setNavigator(commonNavigator)
        val titleContainer: LinearLayout =
            commonNavigator.getTitleContainer()!! // must after setNavigator

        titleContainer.showDividers = LinearLayout.SHOW_DIVIDER_MIDDLE
        titleContainer.dividerDrawable = object : ColorDrawable() {
            override fun getIntrinsicWidth(): Int {
                return UIUtil.dip2px(this@ViewPagerActivity, 15.0)
            }
        }

        val fragmentContainerHelper = FragmentContainerHelper()
        fragmentContainerHelper.init(magicIndicator)
        fragmentContainerHelper.setInterpolator(OvershootInterpolator(2.0f))
        fragmentContainerHelper.setDuration(300)

        // 注册页面变化的回调接口
        viewPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                fragmentContainerHelper.handlePageSelected(position)
            }
        })
    }
}