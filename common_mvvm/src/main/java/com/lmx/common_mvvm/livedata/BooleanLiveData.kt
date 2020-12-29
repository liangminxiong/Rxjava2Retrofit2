package com.lmx.common_mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by lmx on 2020/5/7
 * Describe : 自定义的Boolean类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class BooleanLiveData(value: Boolean = false) : MutableLiveData<Boolean>(value) {

    override fun getValue(): Boolean {
        return super.getValue()!!
    }
}