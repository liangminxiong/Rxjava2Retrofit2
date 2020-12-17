package com.lmx.magicindicator.abs

/**
 * Created by lmx on 2020/12/14
 * Describe:自定义滚动状态，消除对ViewPager的依赖
 */
interface ScrollState {
    companion object {
        val SCROLL_STATE_IDLE: Int get() = 0
        val SCROLL_STATE_DRAGGING: Int get() = 1
        val SCROLL_STATE_SETTLING: Int get() = 2
    }
}