package com.lmx.common_mvvm.network

/**
 * Created by lmx on 2020/5/7
 * Describe : 自定义错误信息异常
 */
class AppException : Exception {

    var errorMsg: String //错误消息
    var errCode: Int = 0 //错误码
    var errorLog: String? //错误日志

    constructor(errCode: Int, error: String?, errorLog: String? = "") : super(error) {
        this.errorMsg = error ?: "请求失败，请稍后再试"
        this.errCode = errCode
        this.errorLog = errorLog?:this.errorMsg
    }

    constructor(error: Error, e: Throwable?) {
        errCode = error.code
        errorMsg = error.errorMsg.toString()
        errorLog = e?.message
    }
}