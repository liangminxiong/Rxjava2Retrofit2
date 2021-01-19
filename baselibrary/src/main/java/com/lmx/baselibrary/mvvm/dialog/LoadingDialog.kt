package com.lmx.baselibrary.mvvm.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.text.TextUtils
import com.lmx.baselibrary.R
import kotlinx.android.synthetic.main.dialog_loading.*

/**
 * Created by lmx on 2021/1/19
 * Describe:
 */
@Suppress("DEPRECATION")
open class LoadingDialog(context: Context, private var isCancel: Boolean) : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_loading)
        setCanceledOnTouchOutside(isCancel)
    }

    open fun showDialog(msg: String?) {
        try {
            if (!isShowing) {
                show()
            }
            if (!TextUtils.isEmpty(msg)) {
                tvLoading.text = msg
            }
        } catch (ex: java.lang.Exception) {
            ex.printStackTrace()
        }
    }


    /**
     * delay 毫秒
     */
    fun showAutoDismiss(msg: String?, delay: Long) {
        showDialog(msg)
        Handler().postDelayed({ dismiss() }, delay)
    }

}