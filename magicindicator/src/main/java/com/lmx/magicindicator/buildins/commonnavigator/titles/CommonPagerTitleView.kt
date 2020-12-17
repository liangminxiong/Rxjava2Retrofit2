package com.lmx.magicindicator.buildins.commonnavigator.titles

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.lmx.magicindicator.abs.IMeasurablePagerTitleView

/**
 * Created by lmx on 2020/12/16
 * Describe:通用的指示器标题，子元素内容由外部提供，事件回传给外部
 */
class CommonPagerTitleView(context: Context) : FrameLayout(context),
    IMeasurablePagerTitleView {

    private var mOnPagerTitleChangeListener: OnPagerTitleChangeListener? = null
    private var mContentPositionDataProvider: ContentPositionDataProvider? = null

    override fun onSelected(index: Int, totalCount: Int) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener!!.onSelected(index, totalCount)
        }
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener!!.onDeselected(index, totalCount)
        }
    }

    override fun onLeave(
        index: Int,
        totalCount: Int,
        leavePercent: Float,
        leftToRight: Boolean
    ) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener!!.onLeave(index, totalCount, leavePercent, leftToRight)
        }
    }

    override fun onEnter(
        index: Int,
        totalCount: Int,
        enterPercent: Float,
        leftToRight: Boolean
    ) {
        if (mOnPagerTitleChangeListener != null) {
            mOnPagerTitleChangeListener!!.onEnter(index, totalCount, enterPercent, leftToRight)
        }
    }

    override fun getContentLeft(): Int {
        return if (mContentPositionDataProvider != null) {
            mContentPositionDataProvider!!.contentLeft
        } else left
    }

    override fun getContentTop(): Int {
        return if (mContentPositionDataProvider != null) {
            mContentPositionDataProvider!!.contentTop
        } else top
    }

    override fun getContentRight(): Int {
        return if (mContentPositionDataProvider != null) {
            mContentPositionDataProvider!!.contentRight
        } else right
    }

    override fun getContentBottom(): Int {
        return if (mContentPositionDataProvider != null) {
            mContentPositionDataProvider!!.contentBottom
        } else bottom
    }

    /**
     * 外部直接将布局设置进来
     *
     * @param contentView
     */
    fun setContentView(contentView: View?) {
        setContentView(contentView, null)
    }

    fun setContentView(
        contentView: View?,
        lp: LayoutParams?
    ) {
        var lp = lp
        removeAllViews()
        if (contentView != null) {
            if (lp == null) {
                lp = LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
            addView(contentView, lp)
        }
    }

    fun setContentView(layoutId: Int) {
        val child = LayoutInflater.from(context).inflate(layoutId, null)
        setContentView(child, null)
    }

    fun getOnPagerTitleChangeListener(): OnPagerTitleChangeListener? {
        return mOnPagerTitleChangeListener
    }

    fun setOnPagerTitleChangeListener(onPagerTitleChangeListener: OnPagerTitleChangeListener?) {
        mOnPagerTitleChangeListener = onPagerTitleChangeListener
    }

    fun getContentPositionDataProvider(): ContentPositionDataProvider? {
        return mContentPositionDataProvider
    }

    fun setContentPositionDataProvider(contentPositionDataProvider: ContentPositionDataProvider?) {
        mContentPositionDataProvider = contentPositionDataProvider
    }

    interface OnPagerTitleChangeListener {
        fun onSelected(index: Int, totalCount: Int)
        fun onDeselected(index: Int, totalCount: Int)
        fun onLeave(
            index: Int,
            totalCount: Int,
            leavePercent: Float,
            leftToRight: Boolean
        )

        fun onEnter(
            index: Int,
            totalCount: Int,
            enterPercent: Float,
            leftToRight: Boolean
        )
    }

    interface ContentPositionDataProvider {
        val contentLeft: Int
        val contentTop: Int
        val contentRight: Int
        val contentBottom: Int
    }
}