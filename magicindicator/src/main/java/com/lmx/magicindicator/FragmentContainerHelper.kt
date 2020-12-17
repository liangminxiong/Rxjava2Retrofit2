package com.lmx.magicindicator

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.annotation.TargetApi
import android.os.Build
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import com.lmx.magicindicator.abs.ScrollState
import com.lmx.magicindicator.buildins.PositionData

/**
 * Created by lmx on 2020/12/14
 * Describe:使得MagicIndicator在FragmentContainer中使用
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class FragmentContainerHelper() {


    private var mMagicIndicators = mutableListOf<MagicIndicator>()
    private var mScrollAnimator: ValueAnimator? = null
    private var mLastSelectedIndex = 0
    private var mDuration = 150
    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()

    fun init(magicIndicator: MagicIndicator) {
        mMagicIndicators.add(magicIndicator)
    }

    private val mAnimatorListener: Animator.AnimatorListener =
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE)
                mScrollAnimator = null
            }
        }

    private val mAnimatorUpdateListener =
        AnimatorUpdateListener { animation ->
            val positionOffsetSum = animation.animatedValue as Float
            var position = positionOffsetSum.toInt()
            var positionOffset = positionOffsetSum - position
            if (positionOffsetSum < 0) {
                position -= 1
                positionOffset += 1.0f
            }
            dispatchPageScrolled(position, positionOffset, 0)
        }

    /**
     * IPagerIndicator支持弹性效果的辅助方法
     *
     * @param positionDataList
     * @param index
     * @return
     */
    fun getImitativePositionData(
        positionDataList: MutableList<PositionData>,
        index: Int
    ): PositionData? {
        return if (index >= 0 && index <= positionDataList.size - 1) { // 越界后，返回假的PositionData
            positionDataList[index]
        } else {
            val result = PositionData()
            val referenceData: PositionData
            val offset: Int
            if (index < 0) {
                offset = index
                referenceData = positionDataList[0]
            } else {
                offset = index - positionDataList.size + 1
                referenceData = positionDataList[positionDataList.size - 1]
            }
            result.mLeft = referenceData.mLeft + offset * referenceData.width()
            result.mTop = referenceData.mTop
            result.mRight = referenceData.mRight + offset * referenceData.width()
            result.mBottom = referenceData.mBottom
            result.mContentLeft = referenceData.mContentLeft + offset * referenceData.width()
            result.mContentTop = referenceData.mContentTop
            result.mContentRight = referenceData.mContentRight + offset * referenceData.width()
            result.mContentBottom = referenceData.mContentBottom
            result
        }
    }

    fun handlePageSelected(selectedIndex: Int) {
        handlePageSelected(selectedIndex, true)
    }

    private fun handlePageSelected(selectedIndex: Int, smooth: Boolean) {
        if (mLastSelectedIndex == selectedIndex) {
            return
        }
        if (smooth) {
            if (mScrollAnimator == null || !mScrollAnimator!!.isRunning) {
                dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_SETTLING)
            }
            dispatchPageSelected(selectedIndex)
            var currentPositionOffsetSum = mLastSelectedIndex.toFloat()
            if (mScrollAnimator != null) {
                currentPositionOffsetSum = mScrollAnimator!!.animatedValue as Float
                mScrollAnimator!!.cancel()
                mScrollAnimator = null
            }
            mScrollAnimator = ValueAnimator()
            mScrollAnimator!!.setFloatValues(
                currentPositionOffsetSum,
                selectedIndex.toFloat()
            ) // position = selectedIndex, positionOffset = 0.0f
            mScrollAnimator!!.addUpdateListener(mAnimatorUpdateListener)
            mScrollAnimator!!.addListener(mAnimatorListener)
            mScrollAnimator!!.interpolator = mInterpolator
            mScrollAnimator!!.duration = mDuration.toLong()
            mScrollAnimator!!.start()
        } else {
            dispatchPageSelected(selectedIndex)
            if (mScrollAnimator != null && mScrollAnimator!!.isRunning) {
                dispatchPageScrolled(mLastSelectedIndex, 0.0f, 0)
            }
            dispatchPageScrollStateChanged(ScrollState.SCROLL_STATE_IDLE)
            dispatchPageScrolled(selectedIndex, 0.0f, 0)
        }
        mLastSelectedIndex = selectedIndex
    }

    fun setDuration(duration: Int) {
        mDuration = duration
    }

    fun setInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator ?: AccelerateDecelerateInterpolator()
    }

    fun attachMagicIndicator(magicIndicator: MagicIndicator) {
        mMagicIndicators.add(magicIndicator)
    }

    private fun dispatchPageSelected(pageIndex: Int) {
        for (magicIndicator in mMagicIndicators) {
            magicIndicator.onPageSelected(pageIndex)
        }
    }

    private fun dispatchPageScrollStateChanged(state: Int) {
        for (magicIndicator in mMagicIndicators) {
            magicIndicator.onPageScrollStateChanged(state)
        }
    }

    private fun dispatchPageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
        for (magicIndicator in mMagicIndicators) {
            magicIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels)
        }
    }
}