package com.lmx.common_mvvm.ext

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * Created by lmx on 2020/9/11
 * Describe ：说明Gson解析时不支持泛型。
但是，我们可以用ParameterizedType 来实现
 */
class ParameterizedTypeImpl(val clz: Class<*>) : ParameterizedType {
    override fun getRawType(): Type = List::class.java

    override fun getOwnerType(): Type? = null

    override fun getActualTypeArguments(): Array<Type> = arrayOf(clz)
}