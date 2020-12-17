package com.lmx.magicindicator.buildins.commonnavigator.indicators

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.view.View
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.lmx.magicindicator.FragmentContainerHelper
import com.lmx.magicindicator.UIUtil
import com.lmx.magicindicator.abs.IPagerIndicator
import com.lmx.magicindicator.buildins.PositionData

/**
 * Created by lmx on 2020/12/16
 * Describe:带有小尖角的直线指示器
 */
class TriangularPagerIndicator(context: Context) : View(context), IPagerIndicator {

    private var mPositionDataList: MutableList<PositionData>? = null
    private var mPaint: Paint? = null
    private var mLineHeight = 0
    private var mLineColor = 0
    private var mTriangleHeight = 0
    private var mTriangleWidth = 0
    private var mReverse = false
    private var mYOffset = 0f

    private val mPath = Path()
    private var mStartInterpolator: Interpolator? = LinearInterpolator()
    private var mAnchorX = 0f

    init {
        init(context)
    }

    private fun init(context: Context) {
        mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mPaint!!.style = Paint.Style.FILL
        mLineHeight = UIUtil.dip2px(context, 3.0)
        mTriangleWidth = UIUtil.dip2px(context, 14.0)
        mTriangleHeight = UIUtil.dip2px(context, 8.0)
    }

    override fun onDraw(canvas: Canvas) {
        mPaint!!.color = mLineColor
        if (mReverse) {
            canvas.drawRect(
                0f,
                height - mYOffset - mTriangleHeight,
                width.toFloat(),
                height - mYOffset - mTriangleHeight + mLineHeight,
                mPaint!!
            )
        } else {
            canvas.drawRect(
                0f,
                height - mLineHeight - mYOffset,
                width.toFloat(),
                height - mYOffset,
                mPaint!!
            )
        }
        mPath.reset()
        if (mReverse) {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, height - mYOffset - mTriangleHeight)
            mPath.lineTo(mAnchorX, height - mYOffset)
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, height - mYOffset - mTriangleHeight)
        } else {
            mPath.moveTo(mAnchorX - mTriangleWidth / 2, height - mYOffset)
            mPath.lineTo(mAnchorX, height - mTriangleHeight - mYOffset)
            mPath.lineTo(mAnchorX + mTriangleWidth / 2, height - mYOffset)
        }
        mPath.close()
        canvas.drawPath(mPath, mPaint!!)
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
        val leftX: Int = current.mLeft + (current.mRight - current.mLeft) / 2
        val rightX: Int = next.mLeft + (next.mRight - next.mLeft) / 2
        mAnchorX = leftX + (rightX - leftX) * mStartInterpolator!!.getInterpolation(positionOffset)
        invalidate()
    }

    override fun onPageSelected(position: Int) {}

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPositionDataProvide(dataList: MutableList<PositionData>) {
        mPositionDataList = dataList
    }

    fun getLineHeight(): Int {
        return mLineHeight
    }

    fun setLineHeight(lineHeight: Int) {
        mLineHeight = lineHeight
    }

    fun getLineColor(): Int {
        return mLineColor
    }

    fun setLineColor(lineColor: Int) {
        mLineColor = lineColor
    }

    fun getTriangleHeight(): Int {
        return mTriangleHeight
    }

    fun setTriangleHeight(triangleHeight: Int) {
        mTriangleHeight = triangleHeight
    }

    fun getTriangleWidth(): Int {
        return mTriangleWidth
    }

    fun setTriangleWidth(triangleWidth: Int) {
        mTriangleWidth = triangleWidth
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

    fun isReverse(): Boolean {
        return mReverse
    }

    fun setReverse(reverse: Boolean) {
        mReverse = reverse
    }

    fun getYOffset(): Float {
        return mYOffset
    }

    fun setYOffset(yOffset: Float) {
        mYOffset = yOffset
    }
}