package com.lmx.common_mvvm.network

import android.net.ParseException
import com.google.gson.JsonParseException
import com.google.gson.stream.MalformedJsonException
import org.apache.http.conn.ConnectTimeoutException
import org.json.JSONException
import retrofit2.HttpException
import java.net.ConnectException
import javax.net.ssl.SSLHandshakeException

/**
 * Created by lmx on 2020/5/7
 * Describe : 根据异常返回相关的错误信息工具类
 */
object ExceptionHandle {

    fun handleException(e: Throwable?): AppException {
        val ex: AppException
        val error: Error
        e?.let {
            when (it) {
                is HttpException -> {
                    val code: Int = it.code()
                    var msg = e.message
                    when {
                        code == 0 -> {
                            msg = "网络请求失败，请检查网络设置"
                        }
                        code in 300..398 -> {
                            msg = "出现重定向，请检查网络设置"
                        }
                        code == 400 -> {
                            msg = "参数有误"
                        }
                        code == 404 -> {
                            msg = "请求的接口不存在"
                        }
                        code in 401..498 -> {
                            msg = "参数有误"
                        }
                        code == 500 -> {
                            msg = "服务器遇到错误，无法完成请求"
                        }
                        code == 504 -> {
                            msg = "网关超时"
                        }
                        code >= 500 -> {
                            msg = "服务器异常，请稍后再试"
                        }
                    }
                    error = Error(code, msg)
                    ex = AppException(error, e)
                    return ex
                }
                is JsonParseException, is JSONException, is ParseException, is MalformedJsonException -> {
                    error = Error(1000, "数据解析出错")
                    ex = AppException(error, e)
                    return ex
                }
                is ConnectException -> {
                    error = Error(1001, "连接失败")
                    ex = AppException(error, e)
                    return ex
                }
                is javax.net.ssl.SSLException, is SSLHandshakeException -> {
                    error = Error(1002, "网络请求失败")
                    ex = AppException(error, e)
                    return ex
                }
                is ConnectTimeoutException, is java.net.SocketTimeoutException -> {
                    error = Error(1003, "连接超时")
                    ex = AppException(error, e)
                    return ex
                }
                is java.net.UnknownHostException -> {
                    error = Error(1004, "网络请求失败，请检查网络设置")
                    ex = AppException(error, e)
                    return ex
                }
                else -> {
                    error = Error(1005, "出现未知错误")
                    ex = AppException(error, e)
                    return ex
                }
            }
        }
        error = Error(1006, "出现未知错误")
        ex = AppException(error, e)
        return ex
    }
}