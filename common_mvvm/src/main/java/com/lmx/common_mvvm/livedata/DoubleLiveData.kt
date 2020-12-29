package com.lmx.common_mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by lmx on 2020/5/7
 * Describe : 自定义的Double类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class DoubleLiveData(var value: Double = 0.0) : MutableLiveData<Double>(value) {

    override fun getValue(): Double {
        return super.getValue()!!
    }
}