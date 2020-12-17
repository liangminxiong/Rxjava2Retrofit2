package com.lmx.magicindicator

import android.content.Context

/**
 * Created by lmx on 2020/12/15
 * Describe:
 */
object UIUtil {

    fun dip2px(context: Context, dpValue: Double): Int {
        val density = context.resources.displayMetrics.density
        return (dpValue * density + 0.5).toInt()
    }

    fun getScreenWidth(context: Context): Int {
        return context.resources.displayMetrics.widthPixels
    }
}