package com.lmx.baselibrary.mvvm.network.stateCallback

/**
 * Created by lmx on 2020/5/7
 * Describe :操作数据的状态类
 */
data class UpdateUiState<T>(
    //请求是否成功
    var isSuccess: Boolean = true,
    //操作的对象
    var data : T,
    //请求失败的错误信息
    var errorMsg: String = ""
)