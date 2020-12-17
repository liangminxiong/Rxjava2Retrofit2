package com.lmx.magicindicator.buildins

import android.content.Context
import android.database.DataSetObservable
import android.database.DataSetObserver
import com.lmx.magicindicator.abs.IPagerIndicator
import com.lmx.magicindicator.abs.IPagerTitleView

/**
 * Created by lmx on 2020/12/16
 * Describe: CommonNavigator适配器，通过它可轻松切换不同的指示器样式
 */
abstract class CommonNavigatorAdapter {

    private var mDataSetObservable = DataSetObservable()

    abstract fun getCount(): Int

    abstract fun getTitleView(context: Context?, index: Int): IPagerTitleView?

    abstract fun getIndicator(context: Context?): IPagerIndicator?

    fun getTitleWeight(context: Context?, index: Int): Float {
        return 1f
    }

    fun registerDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.registerObserver(observer)
    }

    fun unregisterDataSetObserver(observer: DataSetObserver) {
        mDataSetObservable.unregisterObserver(observer)
    }

    fun notifyDataSetChanged() {
        mDataSetObservable.notifyChanged()
    }

    fun notifyDataSetInvalidated() {
        mDataSetObservable.notifyInvalidated()
    }
}