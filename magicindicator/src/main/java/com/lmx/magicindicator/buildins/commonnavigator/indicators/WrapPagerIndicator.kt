package com.lmx.magicindicator.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.lmx.magicindicator.FragmentContainerHelper
import com.lmx.magicindicator.UIUtil
import com.lmx.magicindicator.abs.IPagerIndicator
import com.lmx.magicindicator.buildins.PositionData

/**
 * Created by lmx on 2020/12/16
 * Describe:包裹住内容区域的指示器，类似天天快报的切换效果，需要和IMeasurablePagerTitleView配合使用
 */
class WrapPagerIndicator(context: Context) : View(context), IPagerIndicator {

    private var mVerticalPadding = 0
    private var mHorizontalPadding = 0
    private var mFillColor = 0
    private var mRoundRadius = 0f
    private var mStartInterpolator: Interpolator? = LinearInterpolator()
    private var mEndInterpolator: Interpolator? = LinearInterpolator()

    private var mPositionDataList: MutableList<PositionData>? = null
    private var mPaint: Paint? = null

    private val mRect = RectF()
    private var mRoundRadiusSet = false

    init {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mVerticalPadding = UIUtil.dip2px(context, 6.0)
        mHorizontalPadding = UIUtil.dip2px(context, 10.0)
    }

    override fun onDraw(canvas: Canvas) {
        mPaint!!.color = mFillColor
        canvas.drawRoundRect(mRect, mRoundRadius, mRoundRadius, mPaint!!)
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        if (mPositionDataList == null || mPositionDataList!!.isEmpty()) {
            return
        }

        // 计算锚点位置
        val current: PositionData =
            FragmentContainerHelper().getImitativePositionData(mPositionDataList!!, position)!!
        val next: PositionData =
            FragmentContainerHelper().getImitativePositionData(mPositionDataList!!, position + 1)!!
        mRect.left =
            current.mContentLeft - mHorizontalPadding + (next.mContentLeft - current.mContentLeft) * mEndInterpolator!!.getInterpolation(
                positionOffset
            )
        mRect.top = (current.mContentTop - mVerticalPadding).toFloat()
        mRect.right =
            current.mContentRight + mHorizontalPadding + (next.mContentRight - current.mContentRight) * mStartInterpolator!!.getInterpolation(
                positionOffset
            )
        mRect.bottom = (current.mContentBottom + mVerticalPadding).toFloat()
        if (!mRoundRadiusSet) {
            mRoundRadius = mRect.height() / 2
        }
        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: MutableList<PositionData>) {
        mPositionDataList = dataList
    }

    fun getPaint(): Paint? {
        return mPaint
    }

    fun getVerticalPadding(): Int {
        return mVerticalPadding
    }

    fun setVerticalPadding(verticalPadding: Int) {
        mVerticalPadding = verticalPadding
    }

    fun getHorizontalPadding(): Int {
        return mHorizontalPadding
    }

    fun setHorizontalPadding(horizontalPadding: Int) {
        mHorizontalPadding = horizontalPadding
    }

    fun getFillColor(): Int {
        return mFillColor
    }

    fun setFillColor(fillColor: Int) {
        mFillColor = fillColor
    }

    fun getRoundRadius(): Float {
        return mRoundRadius
    }

    fun setRoundRadius(roundRadius: Float) {
        mRoundRadius = roundRadius
        mRoundRadiusSet = true
    }

    fun getStartInterpolator(): Interpolator? {
        return mStartInterpolator
    }

    fun setStartInterpolator(startInterpolator: Interpolator?) {
        mStartInterpolator = startInterpolator
        if (mStartInterpolator == null) {
            mStartInterpolator = LinearInterpolator()
        }
    }

    fun getEndInterpolator(): Interpolator? {
        return mEndInterpolator
    }

    fun setEndInterpolator(endInterpolator: Interpolator?) {
        mEndInterpolator = endInterpolator
        if (mEndInterpolator == null) {
            mEndInterpolator = LinearInterpolator()
        }
    }
}