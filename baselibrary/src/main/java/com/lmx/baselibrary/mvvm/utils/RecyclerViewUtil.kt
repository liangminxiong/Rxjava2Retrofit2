package com.lmx.baselibrary.mvvm.utils

import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator

/**
 * Created by lmx on 2020/9/16
 * Describe ：关闭默认RecyclerView item动画
 */
object RecyclerViewUtil {

    @JvmStatic
    fun changeItemAnimation(recyclerView: RecyclerView, isOpen: Boolean) {
        // 关闭viewholder动画效果。解决viewholder闪烁问题
        val animator: RecyclerView.ItemAnimator = recyclerView.itemAnimator!!
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = isOpen
        }
    }
}