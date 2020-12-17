package com.lmx.magicindicator.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.View
import com.lmx.magicindicator.FragmentContainerHelper
import com.lmx.magicindicator.abs.IPagerIndicator
import com.lmx.magicindicator.buildins.PositionData

/**
 * Created by lmx on 2020/12/16
 * Describe:用于测试的指示器，可用来检测自定义的IMeasurablePagerTitleView是否正确测量内容区域
 */
class TestPagerIndicator(context: Context) : View(context), IPagerIndicator {

    private var mPaint: Paint? = null
    private var mOutRectColor = 0
    private var mInnerRectColor = 0
    private var mOutRect = RectF()
    private var mInnerRect = RectF()

    private var mPositionDataList: MutableList<PositionData>? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.STROKE
        mOutRectColor = Color.RED
        mInnerRectColor = Color.GREEN
    }

    override fun onDraw(canvas: Canvas) {
        mPaint!!.color = mOutRectColor
        canvas.drawRect(mOutRect, mPaint!!)
        mPaint!!.color = mInnerRectColor
        canvas.drawRect(mInnerRect, mPaint!!)
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
        mOutRect.left = current.mLeft + (next.mLeft - current.mLeft) * positionOffset
        mOutRect.top = current.mTop + (next.mTop - current.mTop) * positionOffset
        mOutRect.right = current.mRight + (next.mRight - current.mRight) * positionOffset
        mOutRect.bottom = current.mBottom + (next.mBottom - current.mBottom) * positionOffset
        mInnerRect.left =
            current.mContentLeft + (next.mContentLeft - current.mContentLeft) * positionOffset
        mInnerRect.top =
            current.mContentTop + (next.mContentTop - current.mContentTop) * positionOffset
        mInnerRect.right =
            current.mContentRight + (next.mContentRight - current.mContentRight) * positionOffset
        mInnerRect.bottom =
            current.mContentBottom + (next.mContentBottom - current.mContentBottom) * positionOffset
        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: MutableList<PositionData>) {
        mPositionDataList = dataList
    }

    fun getOutRectColor(): Int {
        return mOutRectColor
    }

    fun setOutRectColor(outRectColor: Int) {
        mOutRectColor = outRectColor
    }

    fun getInnerRectColor(): Int {
        return mInnerRectColor
    }

    fun setInnerRectColor(innerRectColor: Int) {
        mInnerRectColor = innerRectColor
    }
}