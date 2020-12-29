package com.lmx.common_mvvm.network

/**
 * Created by lmx on 2020/5/7
 * Describe :错误枚举类
 */
data class Error(var code: Int, var errorMsg: String?)
//enum class Error(var code: Int, var err: String) {
//    /**
//     * 未知错误
//     */
//    UNKNOWN(1000, "请求失败，请稍后再试"),
//
//    /**
//     * 解析错误
//     */
//    PARSE_ERROR(1001, "解析错误，请稍后再试"),
//
//    /**
//     * 网络错误
//     */
//    NETWORD_ERROR(1002, "网络连接错误，请稍后重试"),
//
//    /**
//     * 证书出错
//     */
//    SSL_ERROR(1004, "证书出错，请稍后再试"),
//
//    /**
//     * 连接超时
//     */
//    TIMEOUT_ERROR(1006, "网络连接超时，请稍后重试");
//
//    fun getValue(): String {
//        return err
//    }
//
//    fun getKey(): Int {
//        return code
//    }
//
//}