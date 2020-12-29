package com.lmx.rxjava2retrofit2

import com.faceunity.FURenderer
import com.lmx.baselibrary.BaseApplication

/**
 * Created by lmx on 2020/12/15
 * Describe: application
 */
class App :BaseApplication(){

    private var app: App? = null

    fun getInstance(): App? {
        return app
    }

    override fun onCreate() {
        super.onCreate()
        app = this
        FURenderer.initFURenderer(this)
    }
}