package com.lmx.magicindicator.buildins.commonnavigator.titles.badge

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.FrameLayout
import com.lmx.magicindicator.abs.IMeasurablePagerTitleView
import com.lmx.magicindicator.abs.IPagerTitleView

/**
 * Created by lmx on 2020/12/16
 * Describe:
 */
class BadgePagerTitleViewMagicIndicator(context: Context) : FrameLayout(context),
    IMeasurablePagerTitleView {

    private var mInnerPagerTitleView: IPagerTitleView? = null
    private var mBadgeView: View? = null
    private var mAutoCancelBadge = true

    private var mXBadgeRule: BadgeRule? = null
    private var mYBadgeRule: BadgeRule? = null


    override fun onSelected(index: Int, totalCount: Int) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView?.onSelected(index, totalCount)
        }
        if (mAutoCancelBadge) {
            setBadgeView(null)
        }
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView?.onDeselected(index, totalCount)
        }
    }

    override fun onLeave(
        index: Int,
        totalCount: Int,
        leavePercent: Float,
        leftToRight: Boolean
    ) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView?.onLeave(index, totalCount, leavePercent, leftToRight)
        }
    }

    override fun onEnter(
        index: Int,
        totalCount: Int,
        enterPercent: Float,
        leftToRight: Boolean
    ) {
        if (mInnerPagerTitleView != null) {
            mInnerPagerTitleView?.onEnter(index, totalCount, enterPercent, leftToRight)
        }
    }

    fun getInnerPagerTitleView(): IPagerTitleView? {
        return mInnerPagerTitleView
    }

    fun setInnerPagerTitleView(innerPagerTitleView: IPagerTitleView) {
        if (mInnerPagerTitleView === innerPagerTitleView) {
            return
        }
        mInnerPagerTitleView = innerPagerTitleView
        removeAllViews()
        if (mInnerPagerTitleView is View) {
            val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            addView(mInnerPagerTitleView as View?, lp)
        }
        if (mBadgeView != null) {
            val lp = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            addView(mBadgeView, lp)
        }
    }

    fun getBadgeView(): View? {
        return mBadgeView
    }

    fun setBadgeView(badgeView: View?) {
        if (mBadgeView === badgeView) {
            return
        }
        mBadgeView = badgeView
        removeAllViews()
        if (mInnerPagerTitleView is View) {
            val lp = LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT
            )
            addView(mInnerPagerTitleView as View?, lp)
        }
        if (mBadgeView != null) {
            val lp = LayoutParams(
                LayoutParams.WRAP_CONTENT,
                LayoutParams.WRAP_CONTENT
            )
            addView(mBadgeView, lp)
        }
    }

    @SuppressLint("DrawAllocation")
    override fun onLayout(
        changed: Boolean,
        left: Int,
        top: Int,
        right: Int,
        bottom: Int
    ) {
        super.onLayout(changed, left, top, right, bottom)
        if (mInnerPagerTitleView is View && mBadgeView != null) {
            val position = IntArray(14) // 14种角标定位方式
            val v = mInnerPagerTitleView as View?
            position[0] = v!!.left
            position[1] = v.top
            position[2] = v.right
            position[3] = v.bottom
            if (mInnerPagerTitleView is IMeasurablePagerTitleView) {
                val view =
                    mInnerPagerTitleView as IMeasurablePagerTitleView?
                position[4] = view!!.getContentLeft()
                position[5] = view.getContentTop()
                position[6] = view.getContentRight()
                position[7] = view.getContentBottom()
            } else {
                for (i in 4..7) {
                    position[i] = position[i - 4]
                }
            }
            position[8] = v.width / 2
            position[9] = v.height / 2
            position[10] = position[4] / 2
            position[11] = position[5] / 2
            position[12] = position[6] + (position[2] - position[6]) / 2
            position[13] = position[7] + (position[3] - position[7]) / 2

            // 根据设置的BadgeRule调整角标的位置
            if (mXBadgeRule != null) {
                val x = position[mXBadgeRule!!.getAnchor()!!.ordinal]
                val offset = mXBadgeRule!!.getOffset()
                val newLeft = x + offset
                mBadgeView!!.offsetLeftAndRight(newLeft - mBadgeView!!.left)
            }
            if (mYBadgeRule != null) {
                val y = position[mYBadgeRule!!.getAnchor()!!.ordinal]
                val offset = mYBadgeRule!!.getOffset()
                val newTop = y + offset
                mBadgeView!!.offsetTopAndBottom(newTop - mBadgeView!!.top)
            }
        }
    }

    override fun getContentLeft(): Int {
        return if (mInnerPagerTitleView is IMeasurablePagerTitleView) {
            left + (mInnerPagerTitleView as IMeasurablePagerTitleView?)!!.getContentLeft()
        } else left
    }

    override fun getContentTop(): Int {
        return if (mInnerPagerTitleView is IMeasurablePagerTitleView) {
            (mInnerPagerTitleView as IMeasurablePagerTitleView?)!!.getContentTop()
        } else top
    }

    override fun getContentRight(): Int {
        return if (mInnerPagerTitleView is IMeasurablePagerTitleView) {
            left + (mInnerPagerTitleView as IMeasurablePagerTitleView?)!!.getContentRight()
        } else right
    }

    override fun getContentBottom(): Int {
        return if (mInnerPagerTitleView is IMeasurablePagerTitleView) {
            (mInnerPagerTitleView as IMeasurablePagerTitleView?)!!.getContentBottom()
        } else bottom
    }

    fun getXBadgeRule(): BadgeRule? {
        return mXBadgeRule
    }

    fun setXBadgeRule(badgeRule: BadgeRule?) {
        if (badgeRule != null) {
            val anchor = badgeRule.getAnchor()
            require(!(anchor !== BadgeAnchor.LEFT && anchor !== BadgeAnchor.RIGHT && anchor !== BadgeAnchor.CONTENT_LEFT && anchor !== BadgeAnchor.CONTENT_RIGHT && anchor !== BadgeAnchor.CENTER_X && anchor !== BadgeAnchor.LEFT_EDGE_CENTER_X && anchor !== BadgeAnchor.RIGHT_EDGE_CENTER_X)) { "x badge rule is wrong." }
        }
        mXBadgeRule = badgeRule
    }

    fun getYBadgeRule(): BadgeRule? {
        return mYBadgeRule
    }

    fun setYBadgeRule(badgeRule: BadgeRule?) {
        if (badgeRule != null) {
            val anchor = badgeRule.getAnchor()
            require(!(anchor !== BadgeAnchor.TOP && anchor !== BadgeAnchor.BOTTOM && anchor !== BadgeAnchor.CONTENT_TOP && anchor !== BadgeAnchor.CONTENT_BOTTOM && anchor !== BadgeAnchor.CENTER_Y && anchor !== BadgeAnchor.TOP_EDGE_CENTER_Y && anchor !== BadgeAnchor.BOTTOM_EDGE_CENTER_Y)) { "y badge rule is wrong." }
        }
        mYBadgeRule = badgeRule
    }

    fun isAutoCancelBadge(): Boolean {
        return mAutoCancelBadge
    }

    fun setAutoCancelBadge(autoCancelBadge: Boolean) {
        mAutoCancelBadge = autoCancelBadge
    }
}