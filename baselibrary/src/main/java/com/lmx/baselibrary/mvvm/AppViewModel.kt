package com.lmx.baselibrary.mvvm

import com.lmx.common_mvvm.BaseViewModel
import com.lmx.common_mvvm.callback.UnPeekLiveData
import com.lmx.baselibrary.mvvm.utils.SettingUtil

/**
 * Created by lmx on 2020/5/7
 * Describe :APP全局的Viewmodel，可以存放公共数据，当他数据改变时，所有监听他的地方都会收到回调,也可以做发送消息
 * 比如 全局可使用的 地理位置信息，账户信息，事件通知等，
 */
class AppViewModel : BaseViewModel() {

    //App 列表动画
    var appAnimation = UnPeekLiveData<Int>()

    init {
        //初始化列表动画
        appAnimation.value = SettingUtil.getListMode()
        //不建议Viewmodel使用Context，但是这是个App级别的Viewmodel，所以使用applacation上下文没事
    }
}