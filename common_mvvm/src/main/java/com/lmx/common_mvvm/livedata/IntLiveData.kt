package com.lmx.common_mvvm.livedata

import androidx.lifecycle.MutableLiveData

/**
 * Created by lmx on 2020/5/7
 * Describe : 自定义的Boolean类型 MutableLiveData  提供了默认值，防止返回的值出现空的情况
 */
class IntLiveData(value: Int = 0) : MutableLiveData<Int>(value) {

    override fun getValue(): Int {
        return super.getValue()!!
    }
}