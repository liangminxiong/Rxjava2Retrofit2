package com.lmx.common_mvvm.databind

import androidx.databinding.ObservableField

/**
 * Created by lmx on 2020/5/6
 * Describe : 自定义的Int类型 ObservableField  提供了默认值，防止返回的值出现空的情况
 */
class IntObservableField(value: Int = 0) : ObservableField<Int>(value) {

    override fun get(): Int {
        return super.get()!!
    }

}