package com.lmx.baselibrary.mvvm.utils

import android.content.Context
import android.content.Context.VIBRATOR_SERVICE
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.preference.PreferenceManager
import android.view.View
import androidx.core.content.ContextCompat
import java.lang.reflect.InvocationTargetException
import kotlin.math.roundToInt

/**
 * Created by lmx on 2020/5/7
 * Describe 设置
 */
object SettingUtil {

    /**
     * 获取当前主题颜色
     */
    fun getColor(context: Context): Int {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        val defaultColor = ContextCompat.getColor(context, android.R.color.white)
        val color = setting.getInt("color", defaultColor)
        return if (color != 0 && Color.alpha(color) != 255) {
            defaultColor
        } else {
            color
        }

    }

    /**
     * 设置主题颜色
     */
    fun setColor(context: Context, color: Int) {
        val setting = PreferenceManager.getDefaultSharedPreferences(context)
        setting.edit().putInt("color", color).apply()
    }

    /**
     * 设置shap文件的颜色
     *
     * @param view
     * @param color
     * @param radius
     */
    fun setShapColor(view: View, color: Int, radius: Float) {
        val drawable = view.background as GradientDrawable
        drawable.setColor(color)
        drawable.cornerRadius = radius
    }

    /**
     * 设置shap的渐变颜色
     */
    fun setShapColor(
        view: View,
        color: IntArray,
        radius: Float,
        orientation: GradientDrawable.Orientation
    ) {
        val drawable = view.background as GradientDrawable
        drawable.orientation = orientation//渐变方向
        drawable.colors = color//渐变颜色数组
        drawable.cornerRadius = radius
    }

    /**
     * 设置selector文件的颜色
     *
     * @param view
     * @param yesColor
     * @param noColor
     */
    fun setSelectorColor(view: View, yesColor: Int, noColor: Int, radius: Float) {
        val mySelectorGrad = view.background as StateListDrawable
        try {
            val slDraClass = StateListDrawable::class.java
            val getStateCountMethod =
                slDraClass.getDeclaredMethod("getStateCount", *arrayOfNulls(0))
            val getStateSetMethod =
                slDraClass.getDeclaredMethod("getStateSet", Int::class.javaPrimitiveType)
            val getDrawableMethod =
                slDraClass.getDeclaredMethod("getStateDrawable", Int::class.javaPrimitiveType)
            val count = getStateCountMethod.invoke(mySelectorGrad) as Int//对应item标签
            for (i in 0 until count) {
                val stateSet = getStateSetMethod.invoke(
                    mySelectorGrad,
                    i
                ) as IntArray//对应item标签中的 android:state_xxxx
                if (stateSet.isEmpty()) {
                    val drawable = getDrawableMethod.invoke(
                        mySelectorGrad,
                        i
                    ) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                    drawable.setColor(yesColor)
                    drawable.cornerRadius = radius
                } else {
                    for (j in stateSet.indices) {
                        val drawable = getDrawableMethod.invoke(
                            mySelectorGrad,
                            i
                        ) as GradientDrawable//这就是你要获得的Enabled为false时候的drawable
                        drawable.setColor(noColor)
                        drawable.cornerRadius = radius
                    }
                }
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        }

    }

    /**
     * 设置颜色透明一半
     * @param color
     * @return
     */
    fun translucentColor(color: Int): Int {
        val factor = 0.5f
        val alpha = (Color.alpha(color) * factor).roundToInt()
        val red = Color.red(color)
        val green = Color.green(color)
        val blue = Color.blue(color)
        return Color.argb(alpha, red, green, blue)
    }

    /**
     * 获取列表动画模式
     */
    fun getListMode(): Int {
        //0 关闭动画 1.渐显 2.缩放 3.从下到上 4.从左到右 5.从右到左
        return 2
    }


    /**
     * 震动一下
     * milliseconds ：时长
     */
    fun startVibrator(context: Context, milliseconds: Long) {
        val vibrator = context.getSystemService(VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val createOneShot =
                VibrationEffect.createOneShot(milliseconds, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(createOneShot)
        } else {
            vibrator.vibrate(milliseconds, null)
        }
    }
}
