package com.lmx.magicindicator.buildins.circlenavigator

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.view.animation.Interpolator
import android.view.animation.LinearInterpolator
import com.lmx.magicindicator.abs.IPagerNavigator
import com.lmx.magicindicator.UIUtil
import kotlin.math.abs

/**
 * Created by lmx on 2020/12/15
 * Describe:
 */
class CircleNavigator(context: Context) : View(context),
    IPagerNavigator {

    private var mRadius = 0
    private var mCircleColor = 0
    private var mStrokeWidth = 0
    private var mCircleSpacing = 0
    private var mCurrentIndex = 0
    private var mTotalCount = 0
    private var mStartInterpolator: Interpolator = LinearInterpolator()

    private var mPaint = Paint(Paint.ANTI_ALIAS_FLAG)
    private var mCirclePoints: MutableList<PointF> = mutableListOf()
    private var mIndicatorX = 0f

    // 事件回调
    private var mTouchable = false
    private var mCircleClickListener: OnCircleClickListener? = null
    private var mDownX = 0f
    private var mDownY = 0f
    private var mTouchSlop = 0

    private var mFollowTouch = true // 是否跟随手指滑动

    init {
        initSlop(context)
    }

    private fun initSlop(context: Context) {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mRadius = UIUtil.dip2px(context, 3.0)
        mCircleSpacing = UIUtil.dip2px(context, 8.0)
        mStrokeWidth = UIUtil.dip2px(context, 1.0)
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec))
    }

    private fun measureWidth(widthMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(widthMeasureSpec)
        val width = MeasureSpec.getSize(widthMeasureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = width
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> result =
                mTotalCount * mRadius * 2 + (mTotalCount - 1) * mCircleSpacing + paddingLeft + paddingRight + mStrokeWidth * 2
            else -> {
            }
        }
        return result
    }

    private fun measureHeight(heightMeasureSpec: Int): Int {
        val mode = MeasureSpec.getMode(heightMeasureSpec)
        val height = MeasureSpec.getSize(heightMeasureSpec)
        var result = 0
        when (mode) {
            MeasureSpec.EXACTLY -> result = height
            MeasureSpec.AT_MOST, MeasureSpec.UNSPECIFIED -> result =
                mRadius * 2 + mStrokeWidth * 2 + paddingTop + paddingBottom
            else -> {
            }
        }
        return result
    }

    override fun onDraw(canvas: Canvas) {
        mPaint.color = mCircleColor
        drawCircles(canvas)
        drawIndicator(canvas)
    }

    private fun drawCircles(canvas: Canvas) {
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mStrokeWidth.toFloat()
        var i = 0
        val j = mCirclePoints.size
        while (i < j) {
            val pointF = mCirclePoints[i]
            canvas.drawCircle(pointF.x, pointF.y, mRadius.toFloat(), mPaint)
            i++
        }
    }

    private fun drawIndicator(canvas: Canvas) {
        mPaint.style = Paint.Style.FILL
        if (mCirclePoints.isNotEmpty()) {
            canvas.drawCircle(
                mIndicatorX,
                (height / 2.0f + 0.5f),
                mRadius.toFloat(),
                mPaint
            )
        }
    }

    private fun prepareCirclePoints() {
        mCirclePoints.clear()
        if (mTotalCount > 0) {
            val y = (height / 2.0f + 0.5f).toInt()
            val centerSpacing = mRadius * 2 + mCircleSpacing
            var startX = mRadius + (mStrokeWidth / 2.0f + 0.5f).toInt() + paddingLeft
            for (i in 0 until mTotalCount) {
                val pointF = PointF(startX.toFloat(), y.toFloat())
                mCirclePoints.add(pointF)
                startX += centerSpacing
            }
            mIndicatorX = mCirclePoints[mCurrentIndex].x
        }
    }

    override fun onPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        if (mFollowTouch) {
            if (mCirclePoints.isEmpty()) {
                return
            }
            val currentPosition = Math.min(mCirclePoints.size - 1, position)
            val nextPosition = Math.min(mCirclePoints.size - 1, position + 1)
            val current = mCirclePoints[currentPosition]
            val next = mCirclePoints[nextPosition]
            mIndicatorX = current.x + (next.x - current.x) * mStartInterpolator.getInterpolation(
                positionOffset
            )
            invalidate()
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y
        when (event.action) {
            MotionEvent.ACTION_DOWN -> if (mTouchable) {
                mDownX = x
                mDownY = y
                return true
            }
            MotionEvent.ACTION_UP -> if (mCircleClickListener != null) {
                if (Math.abs(x - mDownX) <= mTouchSlop && abs(y - mDownY) <= mTouchSlop) {
                    var max = Float.MAX_VALUE
                    var index = 0
                    var i = 0
                    while (i < mCirclePoints.size) {
                        val pointF = mCirclePoints[i]
                        val offset = Math.abs(pointF.x - x)
                        if (offset < max) {
                            max = offset
                            index = i
                        }
                        i++
                    }
                    if (mCircleClickListener != null) {
                        mCircleClickListener?.onClick(index)
                    }
                }
            }
            else -> {
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onPageSelected(position: Int) {
        mCurrentIndex = position
        if (!mFollowTouch) {
            mIndicatorX = mCirclePoints[mCurrentIndex].x
            invalidate()
        }
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        prepareCirclePoints()
    }

    override fun onAttachToMagicIndicator() {}

    override fun notifyDataSetChanged() {
        prepareCirclePoints()
        invalidate()
    }

    override fun onDetachFromMagicIndicator() {}

    fun getRadius(): Int {
        return mRadius
    }

    fun setRadius(radius: Int) {
        mRadius = radius
        prepareCirclePoints()
        invalidate()
    }

    fun getCircleColor(): Int {
        return mCircleColor
    }

    fun setCircleColor(circleColor: Int) {
        mCircleColor = circleColor
        invalidate()
    }

    fun getStrokeWidth(): Int {
        return mStrokeWidth
    }

    fun setStrokeWidth(strokeWidth: Int) {
        mStrokeWidth = strokeWidth
        invalidate()
    }

    fun getCircleSpacing(): Int {
        return mCircleSpacing
    }

    fun setCircleSpacing(circleSpacing: Int) {
        mCircleSpacing = circleSpacing
        prepareCirclePoints()
        invalidate()
    }

    fun getStartInterpolator(): Interpolator? {
        return mStartInterpolator
    }

    fun setStartInterpolator(startInterpolator: Interpolator) {
        mStartInterpolator = startInterpolator
        if (mStartInterpolator == null) {
            mStartInterpolator = LinearInterpolator()
        }
    }

    fun getCircleCount(): Int {
        return mTotalCount
    }

    fun setCircleCount(count: Int) {
        mTotalCount = count // 此处不调用invalidate，让外部调用notifyDataSetChanged
    }

    fun isTouchable(): Boolean {
        return mTouchable
    }

    fun setTouchable(touchable: Boolean) {
        mTouchable = touchable
    }

    fun isFollowTouch(): Boolean {
        return mFollowTouch
    }

    fun setFollowTouch(followTouch: Boolean) {
        mFollowTouch = followTouch
    }

    fun getCircleClickListener(): OnCircleClickListener? {
        return mCircleClickListener
    }

    fun setCircleClickListener(circleClickListener: OnCircleClickListener) {
        if (!mTouchable) {
            mTouchable = true
        }
        mCircleClickListener = circleClickListener
    }

    interface OnCircleClickListener {
        fun onClick(index: Int)
    }
}