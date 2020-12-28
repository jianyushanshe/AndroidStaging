package com.jianyushanshe.androidstaging

import android.app.Application
import android.content.Context
import androidx.multidex.MultiDex
import com.tencent.bugly.crashreport.CrashReport

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
        CrashReport.initCrashReport(this, BuildConfig.buglyId, BuildConfig.LOG_DEBUG)
    }

    companion object {
        lateinit var context: Context
    }
}