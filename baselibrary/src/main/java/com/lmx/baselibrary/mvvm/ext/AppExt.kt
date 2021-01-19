package com.lmx.baselibrary.mvvm.ext

import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.lmx.baselibrary.BaseApplication
import com.lmx.baselibrary.mvvm.AppViewModel
import com.lmx.baselibrary.mvvm.utils.AppUtils
import com.lmx.common_mvvm.Ktx
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

/**
 * Created by lmx on 2020/5/7
 * Describe
 */
fun AppCompatActivity.getAppViewModel(): AppViewModel {
    (AppUtils.getApp() as BaseApplication).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

fun Fragment.getAppViewModel(): AppViewModel {
    (Ktx.app as BaseApplication).let {
        return it.getAppViewModelProvider().get(AppViewModel::class.java)
    }
}

/**
 * 获取进程号对应的进程名
 *
 * @param pid 进程号
 * @return 进程名
 */
fun getProcessName(pid: Int): String? {
    var reader: BufferedReader? = null
    try {
        reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
        var processName = reader.readLine()
        if (!TextUtils.isEmpty(processName)) {
            processName = processName.trim { it <= ' ' }
        }
        return processName
    } catch (throwable: Throwable) {
        throwable.printStackTrace()
    } finally {
        try {
            reader?.close()
        } catch (exception: IOException) {
            exception.printStackTrace()
        }

    }
    return null
}
