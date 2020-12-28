package com.jianyushanshe.androidstaging.ui.home

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.BaseActivity
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.ui.mine.StationListActivity
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
        tv_list.setOnClickListener {
           openActivity<StationListActivity>(this)
        }
        tv_bugly.setOnClickListener {
            CrashReport.testJavaCrash()
        }
    }
}