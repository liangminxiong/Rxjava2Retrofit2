package com.lmx.magicindicator.buildins.commonnavigator.titles

import android.content.Context
import com.lmx.magicindicator.ArgbEvaluatorHolder

/**
 * Created by lmx on 2020/12/16
 * Describe:两种颜色过渡的指示器标题
 */
class ColorTransitionPagerTitleView(context: Context) : SimplePagerTitleView(context) {

    override fun onLeave(
        index: Int,
        totalCount: Int,
        leavePercent: Float,
        leftToRight: Boolean
    ) {
        val color: Int = ArgbEvaluatorHolder.eval(leavePercent, mSelectedColor, mNormalColor)
        setTextColor(color)
    }

    override fun onEnter(
        index: Int,
        totalCount: Int,
        enterPercent: Float,
        leftToRight: Boolean
    ) {
        val color: Int = ArgbEvaluatorHolder.eval(enterPercent, mNormalColor, mSelectedColor)
        setTextColor(color)
    }

    override fun onSelected(index: Int, totalCount: Int) {}

    override fun onDeselected(index: Int, totalCount: Int) {}
}