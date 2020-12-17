package com.lmx.costomview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import java.util.*
import kotlin.math.roundToInt

/**
 * Created by lmx on 2020/12/17
 * Describe:自动换行布局
 */
class WrapLayout : ViewGroup {

    companion object {
        const val GRAVITY_PARENT = -1 // 使用全局对齐方案

        const val GRAVITY_TOP = 0 // 子项顶部对齐

        const val GRAVITY_CENTER = 1 // 子项居中对齐

        const val GRAVITY_BOTTOM = 2 // 子项底部对齐

        private val ATTRS =
            intArrayOf(android.R.attr.horizontalSpacing, android.R.attr.verticalSpacing)
    }

    private var mVerticalSpacing = 0
    private var mHorizontalSpacing = 0
    private var mNumRows = 0
    private var mNumColumns = ArrayList<Int>()
    private var mChildMaxWidth = ArrayList<Int>()
    private var mGravity = GRAVITY_TOP

    constructor(context: Context) : super(context) {
        initView(context, null, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet) : super(context, attributeSet) {
        initView(context, attributeSet, 0)
    }

    constructor(context: Context, attributeSet: AttributeSet, defStyleAttr: Int) : super(
        context,
        attributeSet,
        defStyleAttr
    ) {
        initView(context, attributeSet, defStyleAttr)
    }

    private fun initView(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) {
        val a = context.obtainStyledAttributes(attrs, ATTRS, defStyleAttr, 0)
        val n = a.indexCount
        var horizontalSpacing = 0
        var verticalSpacing = 0
        for (i in 0 until n) {
            when (val attr = a.getIndex(i)) {
                0 -> horizontalSpacing = a.getDimensionPixelSize(attr, horizontalSpacing)
                1 -> verticalSpacing = a.getDimensionPixelSize(attr, verticalSpacing)
            }
        }
        a.recycle()
        val custom = context.obtainStyledAttributes(attrs, R.styleable.WrapLayout)
        horizontalSpacing = custom.getDimensionPixelSize(
            R.styleable.WrapLayout_wlyHorizontalSpacing, horizontalSpacing
        )
        verticalSpacing = custom.getDimensionPixelSize(
            R.styleable.WrapLayout_wlyVerticalSpacing, verticalSpacing
        )
        val gravity = custom.getInt(R.styleable.WrapLayout_wlyGravity, GRAVITY_TOP)
        custom.recycle()
        mHorizontalSpacing = horizontalSpacing
        mVerticalSpacing = verticalSpacing
        mGravity = gravity
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams? {
        return LayoutParams(context, attrs)
    }

    /**
     * Returns a set of layout parameters with a width of
     * [ViewGroup.LayoutParams.WRAP_CONTENT],
     * a height of [ViewGroup.LayoutParams.WRAP_CONTENT] and no spanning.
     */
    override fun generateDefaultLayoutParams(): ViewGroup.LayoutParams? {
        return LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    // Override to allow type-checking of LayoutParams.
    override fun checkLayoutParams(p: ViewGroup.LayoutParams?): Boolean {
        return p is LayoutParams
    }

    override fun generateLayoutParams(lp: ViewGroup.LayoutParams?): ViewGroup.LayoutParams? {
        return if (lp is LayoutParams) {
            LayoutParams(lp)
        } else {
            LayoutParams(lp)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val paddingStart = paddingStart
        val paddingEnd = paddingEnd
        val paddingTop = paddingTop
        val paddingBottom = paddingBottom
        val suggestedMinimumWidth = suggestedMinimumWidth
        val suggestedMinimumHeight = suggestedMinimumHeight
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        var itemsWidth = 0
        var itemsHeight = 0
        mNumRows = 0
        mNumColumns.clear()
        mChildMaxWidth.clear()
        if (childCount > 0) {
            if (widthMode == MeasureSpec.UNSPECIFIED) {
                var numColumns = 0
                for (index in 0 until childCount) {
                    val child = getChildAt(index)
                    if (child.visibility == View.GONE) {
                        continue
                    }
                    if (mNumRows == 0) mNumRows = 1
                    measureChild(child, widthMeasureSpec, heightMeasureSpec)
                    val childWidth = child.measuredWidth
                    val childHeight = child.measuredHeight
                    if (numColumns == 0) {
                        itemsWidth = -mHorizontalSpacing
                    }
                    itemsWidth += mHorizontalSpacing + childWidth
                    itemsHeight = childHeight.coerceAtLeast(itemsHeight)
                    numColumns++
                }
                if (numColumns != 0) {
                    mNumColumns.add(numColumns)
                    mChildMaxWidth.add(itemsHeight)
                }
            } else {
                var numColumns = 0
                val maxItemsWidth = widthSize - paddingStart - paddingEnd
                var rowWidth = 0
                var rowHeight = 0
                for (index in 0 until childCount) {
                    val child = getChildAt(index)
                    if (child.visibility == View.GONE) {
                        continue
                    }
                    if (mNumRows == 0) mNumRows = 1
                    measureChild(child, widthMeasureSpec, heightMeasureSpec)
                    val childWidth = child.measuredWidth
                    val childHeight = child.measuredHeight
                    if (numColumns == 0) {
                        rowWidth = -mHorizontalSpacing
                    }
                    if (rowWidth + childWidth + mHorizontalSpacing <= maxItemsWidth) {
                        rowWidth += childWidth + mHorizontalSpacing
                        rowHeight = childHeight.coerceAtLeast(rowHeight)
                        numColumns++
                    } else {
                        itemsWidth = rowWidth.coerceAtLeast(itemsWidth)
                        itemsHeight += if (mNumRows == 1) rowHeight else mVerticalSpacing + rowHeight
                        mNumColumns.add(numColumns)
                        mChildMaxWidth.add(rowHeight)
                        mNumRows++
                        rowWidth = 0
                        rowHeight = 0
                        numColumns = 0
                        rowWidth += childWidth
                        rowHeight = childHeight.coerceAtLeast(rowHeight)
                        numColumns++
                    }
                }
                if (numColumns != 0) {
                    itemsHeight += if (mNumRows == 1) rowHeight else mVerticalSpacing + rowHeight
                    mNumColumns.add(numColumns)
                    mChildMaxWidth.add(rowHeight)
                }
            }
        }
        itemsWidth =
            (paddingStart + itemsWidth + paddingEnd).coerceAtLeast(suggestedMinimumWidth)
        itemsHeight =
            (paddingTop + itemsHeight + paddingBottom).coerceAtLeast(suggestedMinimumHeight)
        setMeasuredDimension(
            View.resolveSize(itemsWidth, widthMeasureSpec),
            View.resolveSize(itemsHeight, heightMeasureSpec)
        )
    }

    override fun onLayout(
        changed: Boolean,
        l: Int,
        t: Int,
        r: Int,
        b: Int
    ) {
        val paddingStart = paddingStart
        val paddingTop = paddingTop
        val gravity = mGravity
        var numChild = 0
        var columnTop = paddingTop - mVerticalSpacing
        for (row in 0 until mNumRows) {
            val numColumn = mNumColumns[row]
            val childMaxHeight = mChildMaxWidth[row]
            var startX = paddingStart - mHorizontalSpacing
            var index = 0
            while (index < numColumn) {
                val childView = getChildAt(numChild)
                numChild++
                if (childView == null || childView.visibility == View.GONE) {
                    continue
                }
                val childWidth = childView.measuredWidth
                val childHeight = childView.measuredHeight
                val layoutParams = childView.layoutParams as LayoutParams
                val childGravity = layoutParams.gravity
                startX += mHorizontalSpacing
                var topOffset: Int
                topOffset = when (childGravity) {
                    GRAVITY_PARENT -> {
                        when (gravity) {
                            GRAVITY_CENTER -> ((childMaxHeight - childHeight) * 0.5f).roundToInt()
                            GRAVITY_BOTTOM -> childMaxHeight - childHeight
                            GRAVITY_TOP -> 0
                            else -> 0
                        }
                    }
                    GRAVITY_CENTER -> ((childMaxHeight - childHeight) * 0.5f).roundToInt()
                    GRAVITY_BOTTOM -> childMaxHeight - childHeight
                    GRAVITY_TOP -> 0
                    else -> {
                        when (gravity) {
                            GRAVITY_CENTER -> ((childMaxHeight - childHeight) * 0.5f).roundToInt()
                            GRAVITY_BOTTOM -> childMaxHeight - childHeight
                            GRAVITY_TOP -> 0
                            else -> 0
                        }
                    }
                }
                val startY = columnTop + mVerticalSpacing + topOffset
                childView.layout(startX, startY, startX + childWidth, startY + childHeight)
                startX += childWidth
                index++
            }
            columnTop += mVerticalSpacing + childMaxHeight
        }
    }

    /**
     * 获取水平间距
     *
     * @return 水平间距
     */
    fun getHorizontalSpacing(): Int {
        return mHorizontalSpacing
    }

    /**
     * 设置水平间距
     *
     * @param pixelSize 水平间距
     */
    fun setHorizontalSpacing(pixelSize: Int) {
        mHorizontalSpacing = pixelSize
        requestLayout()
    }

    /**
     * 获取垂直间距
     *
     * @return 垂直间距
     */
    fun getVerticalSpacing(): Int {
        return mVerticalSpacing
    }

    /**
     * 设置垂直间距
     *
     * @param pixelSize 垂直间距
     */
    fun setVerticalSpacing(pixelSize: Int) {
        mVerticalSpacing = pixelSize
        requestLayout()
    }

    /**
     * 获取行数目
     *
     * @return 行数目
     */
    fun getNumRows(): Int {
        return mNumRows
    }

    /**
     * 获取某一行列数目
     *
     * @param index 行号
     * @return 列数目
     */
    fun getNumColumns(index: Int): Int {
        val numColumns = -1
        return if (index < 0 || index >= mNumColumns.size) {
            numColumns
        } else mNumColumns[index]
    }

    /**
     * 获取子项对齐模式
     *
     * @return 对齐模式
     */
    fun getGravity(): Int {
        return mGravity
    }

    /**
     * 设置子项对齐模式
     *
     * @param gravity 对齐模式
     */
    fun setGravity(gravity: Int) {
        if (gravity != GRAVITY_TOP && gravity != GRAVITY_CENTER && gravity != GRAVITY_BOTTOM) return
        mGravity = gravity
        requestLayout()
    }

    /**
     * Per-child layout information associated with WrapLayout.
     */
    class LayoutParams : ViewGroup.LayoutParams {
        /**
         * 获取布局
         *
         * @return 布局
         */
        /**
         * 设置布局
         *
         * @param gravity 布局
         */
        var gravity: Int = GRAVITY_PARENT

        constructor(c: Context, attrs: AttributeSet?) : super(
            c,
            attrs
        ) {
            var gravity: Int = GRAVITY_PARENT
            val custom = c.obtainStyledAttributes(attrs, R.styleable.WrapLayout_Layout)
            gravity = custom.getInt(R.styleable.WrapLayout_Layout_wlyLayout_gravity, gravity)
            custom.recycle()
            this.gravity = gravity
        }

        constructor(width: Int, height: Int) : super(width, height) {}
        constructor(source: ViewGroup.LayoutParams?) : super(source) {}
        constructor(source: LayoutParams) : super(source) {
            gravity = source.gravity
        }

    }
}