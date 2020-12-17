package com.lmx.magicindicator.buildins.commonnavigator.titles

import android.content.Context
import android.graphics.Rect
import android.text.TextUtils
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.lmx.magicindicator.UIUtil
import com.lmx.magicindicator.abs.IMeasurablePagerTitleView

/**
 * Created by lmx on 2020/12/16
 * Describe:带文本的指示器标题
 */
open class SimplePagerTitleView(context: Context) : AppCompatTextView(context),
    IMeasurablePagerTitleView {

    protected var mSelectedColor = 0
    protected var mNormalColor = 0

    init {
        init(context)
    }

    private fun init(context: Context) {
        gravity = Gravity.CENTER
        val padding: Int = UIUtil.dip2px(context, 10.0)
        setPadding(padding, 0, padding, 0)
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.END
    }

    override fun onSelected(index: Int, totalCount: Int) {
        setTextColor(mSelectedColor)
    }

    override fun onDeselected(index: Int, totalCount: Int) {
        setTextColor(mNormalColor)
    }

    override fun onLeave(
        index: Int,
        totalCount: Int,
        leavePercent: Float,
        leftToRight: Boolean
    ) {
    }

    override fun onEnter(
        index: Int,
        totalCount: Int,
        enterPercent: Float,
        leftToRight: Boolean
    ) {
    }

    override fun getContentLeft(): Int {
        val bound = Rect()
        var longestString = ""
        if (text.toString().contains("\n")) {
            val brokenStrings =
                text.toString().split("\\n").toTypedArray()
            for (each in brokenStrings) {
                if (each.length > longestString.length) longestString = each
            }
        } else {
            longestString = text.toString()
        }
        paint.getTextBounds(longestString, 0, longestString.length, bound)
        val contentWidth = bound.width()
        return left + width / 2 - contentWidth / 2
    }

    override fun getContentTop(): Int {
        val metrics = paint.fontMetrics
        val contentHeight = metrics.bottom - metrics.top
        return (height / 2 - contentHeight / 2).toInt()
    }

    override fun getContentRight(): Int {
        val bound = Rect()
        var longestString = ""
        if (text.toString().contains("\n")) {
            val brokenStrings =
                text.toString().split("\\n").toTypedArray()
            for (each in brokenStrings) {
                if (each.length > longestString.length) longestString = each
            }
        } else {
            longestString = text.toString()
        }
        paint.getTextBounds(longestString, 0, longestString.length, bound)
        val contentWidth = bound.width()
        return left + width / 2 + contentWidth / 2
    }

    override fun getContentBottom(): Int {
        val metrics = paint.fontMetrics
        val contentHeight = metrics.bottom - metrics.top
        return (height / 2 + contentHeight / 2).toInt()
    }

    fun getSelectedColor(): Int {
        return mSelectedColor
    }

    fun setSelectedColor(selectedColor: Int) {
        mSelectedColor = selectedColor
    }

    fun getNormalColor(): Int {
        return mNormalColor
    }

    fun setNormalColor(normalColor: Int) {
        mNormalColor = normalColor
    }
}