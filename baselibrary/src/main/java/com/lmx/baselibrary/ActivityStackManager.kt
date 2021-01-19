package com.lmx.baselibrary

import android.app.Activity
import android.app.Application
import androidx.collection.ArrayMap

/**
 * Created by lmx on 2020/1/18
 * Describe Activity 栈管理
 */
class ActivityStackManager private constructor() {

    companion object {
        @Volatile
        private var sInstance: ActivityStackManager? = null

        private val mActivitySet = ArrayMap<String, Activity>()

        /** 当前 Activity 对象标记  */
        private var mCurrentTag: String? = null
        private var mTag: String = "mvp"

        fun getInstance(): ActivityStackManager {
            // 加入双重校验锁
            if (sInstance == null) {
                synchronized(ActivityStackManager::class.java) {
                    if (sInstance == null) {
                        sInstance = ActivityStackManager()
                    }
                }
            }
            return sInstance!!
        }
    }

    /**
     * 获取 Application 对象
     */
    fun getApplication(): Application {
        return getTopActivity()!!.application
    }

    /**
     * 获取栈顶的 Activity
     */
    fun getTopActivity(): Activity? {
        return mActivitySet[mCurrentTag]
    }

    /**
     * 销毁所有的 Activity
     */
    fun finishAllActivities() {
        finishAllActivities(null as Class<out Activity>)
    }

    /**
     * 销毁所有的 Activity，除这些 Class 之外的 Activity
     */
    @SafeVarargs
    fun finishAllActivities(vararg classArray: Class<out Activity>) {
        val keys = mActivitySet.keys.toTypedArray()
        for (key in keys) {
            val activity = mActivitySet[key]
            if (activity != null && !activity.isFinishing) {
                var whiteClazz = false
                for (clazz in classArray) {
                    if (activity.javaClass == clazz) {
                        whiteClazz = true
                    }
                }
                // 如果不是白名单上面的 Activity 就销毁掉
                if (!whiteClazz) {
                    activity.finish()
                    mActivitySet.remove(key)
                }
            }
        }
    }

    /**
     * Activity 同名方法回调
     */
    fun onCreated(activity: Activity, tag: String) {
        mCurrentTag = getObjectTag(activity)
        mActivitySet[getObjectTag(activity)] = activity
        mTag = tag
    }

    /**
     * Activity 同名方法回调
     */
    fun getActivityTag(): String {
        return mTag
    }

    /**
     * Activity 同名方法回调
     */
    fun onDestroyed(activity: Activity) {
        mActivitySet.remove(getObjectTag(activity))
        // 如果当前的 Activity 是最后一个的话
        if (getObjectTag(activity) == mCurrentTag) {
            // 清除当前标记
            mCurrentTag = null
        }
    }

    /**
     * 获取一个对象的独立无二的标记
     */
    private fun getObjectTag(`object`: Any): String {
        // 对象所在的包名 + 对象的内存地址
        return `object`.javaClass.name + Integer.toHexString(`object`.hashCode())
    }
}