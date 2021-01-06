package com.jianyushanshe.androidstaging.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jianyushanshe.androidstaging.BuildConfig
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.BaseActivity
import com.jianyushanshe.androidstaging.common.helper.AppUpdateHelper
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.ui.mine.StationListActivity
import com.jianyushanshe.androidstaging.common.webview.CommonWebActivity
import com.jianyushanshe.androidstaging.logic.network.exceptionDispose
import com.jianyushanshe.androidstaging.util.openActivity
import com.tencent.bugly.crashreport.CrashReport
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 首页
 */
class MainActivity : BaseActivity<MainViewModel>() {

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }


    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        setActionbar(CustomActionbar.TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT, "首页", null, 0, 0)
        observer()
        tv_list.setOnClickListener {
           openActivity<StationListActivity>(this)
        }
        tv_bugly.setOnClickListener {
            CrashReport.testJavaCrash()
        }
        tv_webview.setOnClickListener {
            CommonWebActivity.open(this,"https://www.baidu.com/","测试网页加载")
        }
        tv_updata.setOnClickListener {
            viewModel.getVersionCode()//获取版本号
        }
    }


    /**
     * 观察数据变化
     */
    private fun observer() {
        viewModel.versionCodeLiveData.observe(this) {
            if (it.exceptionOrNull() != null) {
                //有异常，处理异常
                exceptionDispose(it.exceptionOrNull())
                return@observe
            }
            //处理正常流程
            val response = it.getOrNull()
            response?.let {
                if (response.minVersionNumber > BuildConfig.VERSION_CODE) {//强制升级
                    AppUpdateHelper.updataApp(
                        this,
                        response.uploadUrl,
                        response.versionNumber,
                        response.versionName,
                        response.forcedUpdatedInstructions ?: "",
                        true
                    )
                    return@let
                }
                if (response.versionNumber > BuildConfig.VERSION_CODE) {//普通升级
                    AppUpdateHelper.updataApp(
                        this,
                        response.uploadUrl,
                        response.versionNumber,
                        response.versionName,
                        response.updatedInstructions ?: "",
                        false
                    )
                    return@let
                }
            }

        }
    }
}