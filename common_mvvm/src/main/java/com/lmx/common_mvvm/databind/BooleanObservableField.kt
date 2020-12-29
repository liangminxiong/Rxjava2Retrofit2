package com.lmx.common_mvvm.databind

import androidx.databinding.ObservableField

/**
 * Created by lmx on 2020/5/6
 * Describe : 自定义的Boolean类型ObservableField 提供了默认值，防止返回的值出现空的情况
 */
class BooleanObservableField(value: Boolean = false) : ObservableField<Boolean>(value) {
    override fun get(): Boolean {
        return super.get()!!
    }

}