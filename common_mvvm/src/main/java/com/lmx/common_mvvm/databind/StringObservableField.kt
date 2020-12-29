package com.lmx.common_mvvm.databind

import androidx.databinding.ObservableField

/**
 * Created by lmx on 2020/5/6
 * Describe : 自定义的String类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class StringObservableField(value: String = "") : ObservableField<String>(value) {

    override fun get(): String {
        return super.get()!!
    }

}