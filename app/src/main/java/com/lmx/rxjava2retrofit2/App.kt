package com.lmx.rxjava2retrofit2

import androidx.multidex.MultiDexApplication

/**
 * Created by lmx on 2020/12/15
 * Describe: application
 */
class App : MultiDexApplication() {

    private var app: App? = null

    fun getInstance(): App? {
        return app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
    }
}