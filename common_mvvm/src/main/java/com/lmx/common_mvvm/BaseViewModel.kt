package com.lmx.common_mvvm

import androidx.lifecycle.ViewModel
import com.lmx.common_mvvm.state.SingleLiveEvent

/**
 * Created by lmx on 2020/5/6
 * Describe: ViewModel的基类
 */
open class BaseViewModel : ViewModel() {

    val uiChange: UiChange by lazy { UiChange() }

    /**
     * 可通知Activity/fragment 做相关Ui操作
     */
    inner class UiChange {
        //显示加载框
        val showDialog by lazy { SingleLiveEvent<String>() }

        //隐藏
        val dismissDialog by lazy { SingleLiveEvent<Void>() }
    }

}