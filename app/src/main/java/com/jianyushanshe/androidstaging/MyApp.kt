package com.jianyushanshe.androidstaging

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex

/**
 * Author:wangjianming
 * Time:2020/12/4 15:19
 * Description:MyApp
 */
class MyApp : Application() {

    init {
        //初始化配置
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        lateinit var context: Context
    }
}