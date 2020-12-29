package com.lmx.common_mvvm.ext

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.lmx.common_mvvm.BaseViewModel
import java.lang.reflect.ParameterizedType

/**
 * Created by lmx on 2020/5/6
 * Describe : 获取vm clazz
 */
@Suppress("UNCHECKED_CAST")
fun <VM> getVmClazz(obj: Any): VM {
    return (obj.javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[0] as VM
}

fun <VM : BaseViewModel> AppCompatActivity.getViewmodel():VM{
    return ViewModelProvider(this).get(getVmClazz(this) as Class<VM>)
}

//重点 list 转字符串
inline fun <reified T> String.toBeanList(): List<T> = Gson().fromJson(this, ParameterizedTypeImpl(T::class.java))
fun Any.toJson(): String = Gson().toJson(this)





