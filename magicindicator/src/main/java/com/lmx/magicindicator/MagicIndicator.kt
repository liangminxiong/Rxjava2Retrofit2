package com.lmx.magicindicator

import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.lmx.magicindicator.abs.IPagerNavigator

/**
 * Created by lmx on 2020/12/14
 * Describe:整个框架的入口，核心
 */
class MagicIndicator(context: Context) : FrameLayout(context) {
    private var mNavigator: IPagerNavigator? = null

    fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (mNavigator != null) {
            mNavigator?.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }

    fun onPageSelected(position: Int) {
        if (mNavigator != null) {
            mNavigator?.onPageSelected(position)
        }
    }

    fun onPageScrollStateChanged(state: Int) {
        if (mNavigator != null) {
            mNavigator?.onPageScrollStateChanged(state)
        }
    }

    fun getNavigator(): IPagerNavigator? {
        return mNavigator
    }

    fun setNavigator(navigator: IPagerNavigator) {
        if (mNavigator === navigator) {
            return
        }
        if (mNavigator != null) {
            mNavigator?.onDetachFromMagicIndicator()
        }
        mNavigator = navigator
        removeAllViews()
        if (mNavigator is View) {
            val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            addView(mNavigator as View?, lp)
            mNavigator?.onAttachToMagicIndicator()
        }
    }
}