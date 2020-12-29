package com.lmx.common_mvvm.ext.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.DynamicDrawableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.ImageSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView

/**
 * 设置view显示
 */
fun View.visible() {
    visibility = View.VISIBLE
}

/**
 * 设置view占位隐藏
 */
fun View.invisible() {
    visibility = View.INVISIBLE
}

/**
 * 设置view隐藏
 */
fun View.gone() {
    visibility = View.GONE
}

/**
 * 将view转为bitmap
 */
@Deprecated("use View.drawToBitmap()")
fun View.toBitmap(scale: Float = 1f, config: Bitmap.Config = Bitmap.Config.ARGB_8888): Bitmap? {
    if (this is ImageView) {
        if (drawable is BitmapDrawable) return (drawable as BitmapDrawable).bitmap
    }
    this.clearFocus()
    val bitmap = createBitmapSafely(
            (width * scale).toInt(),
            (height * scale).toInt(),
            config,
            1
    )
    if (bitmap != null) {
        Canvas().run {
            setBitmap(bitmap)
            save()
            drawColor(Color.WHITE)
            scale(scale, scale)
            this@toBitmap.draw(this)
            restore()
            setBitmap(null)
        }
    }
    return bitmap
}

fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
    try {
        return Bitmap.createBitmap(width, height, config)
    } catch (e: OutOfMemoryError) {
        e.printStackTrace()
        if (retryCount > 0) {
            System.gc()
            return createBitmapSafely(width, height, config, retryCount - 1)
        }
        return null
    }
}


/**
 * 防止重复点击事件 默认0.5秒内不可重复点击
 * @param interval 时间间隔 默认0.5秒
 * @param action 执行方法
 */
var lastClickTime = 0L
fun View.clickNoRepeat(interval: Long = 500, action: (view: View) -> Unit) {
    setOnClickListener {
        val currentTime = System.currentTimeMillis()
        if (lastClickTime != 0L && (currentTime - lastClickTime < interval)) {
            return@setOnClickListener
        }
        lastClickTime = currentTime
        action(it)
    }
}


fun Any?.notNull(notNullAction: () -> Unit, nullAction1: () -> Unit) {
    if (this != null) {
        notNullAction.invoke()
    } else {
        nullAction1.invoke()
    }
}

/**
 * 一个TextView设置多种颜色
 */
fun TextView.setSpanNable(text: String, start: Int, end: Int, color: Int) {
    val style = SpannableStringBuilder(text)
    style.setSpan(ForegroundColorSpan(color), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    setText(style)
}

/**
 * 一个TextView设置多种颜色
 */
fun TextView.setSpanNableMore(text: String, firstStart: Int, firstEnd: Int, firstColor: Int, twoStart: Int, twoEnd: Int, twoColor: Int) {
    val style = SpannableStringBuilder(text)
    style.setSpan(ForegroundColorSpan(firstColor), firstStart, firstEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    style.setSpan(ForegroundColorSpan(twoColor), twoStart, twoEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    setText(style)
}

/**
 * 一个TextView设置多种颜色
 */
fun TextView.setSpanNableMore(text: String, color: Int, firstStart: Int, firstEnd: Int, twoStart: Int, twoEnd: Int, threeStart: Int, threeEnd: Int, fourStart: Int, fourEnd: Int) {
    val style = SpannableStringBuilder(text)
    style.setSpan(ForegroundColorSpan(color), firstStart, firstEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    style.setSpan(ForegroundColorSpan(color), twoStart, twoEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    style.setSpan(ForegroundColorSpan(color), threeStart, threeEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    style.setSpan(ForegroundColorSpan(color), fourStart, fourEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    setText(style)
}

/**
SpannableString span3 = new SpannableString("我如果爱你");
ImageSpan image = new ImageSpan(this,R.drawable.collect, DynamicDrawableSpan.ALIGN_BOTTOM);
span3.setSpan(image,3,4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
tv3.setText(span3);
 */
/**
 * 一个TextView设置图文混排
 */
fun TextView.setImageSpan(context: Context, text: String, start: Int, end: Int, imgedID: Int) {
    val span3 = SpannableString(text)
    val image = ImageSpan(context, imgedID, DynamicDrawableSpan.ALIGN_BOTTOM)
    span3.setSpan(image, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    setText(span3)
}


/**
 * TextView设置文字大小
 */
fun TextView.setSize(size: Int) {
    textSize = size.toFloat()
}

/**
 * TextView设置文字加粗
 */
fun TextView.setTextBold(isTextBold: Boolean) {
    paint.isFakeBoldText = isTextBold
}
