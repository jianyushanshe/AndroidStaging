package com.jianyushanshe.androidstaging.ui.mine

import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.BaseListActivity
import com.jianyushanshe.androidstaging.common.base.CommonBaseAdapter
import com.jianyushanshe.androidstaging.common.base.CommonBaseAdapter.OnItemClickListener
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.extension.showToast
import com.jianyushanshe.androidstaging.logic.network.exceptionDispose
import com.jianyushanshe.androidstaging.util.Constants.LOAD_FOOTER_LIST_VIEW
import com.jianyushanshe.androidstaging.util.Constants.LOAD_FOOTER_NO_MORE_DATA
import com.jianyushanshe.androidstaging.util.Constants.REFRESH_EMPTY
import com.jianyushanshe.androidstaging.util.Constants.REFRESH_FIRST_LIST_VIEW
import com.jianyushanshe.androidstaging.util.getStringResId
import com.scwang.smart.refresh.layout.constant.RefreshState

class StationListActivity : BaseListActivity<StationViewModel>() {

    override fun requestDataOnResume() {
        getFirstData()
    }

    /**
     * 初始化adapter
     */
    override fun initAdapter() {
        mBaseAdapter = OrderAdapter(this) as CommonBaseAdapter<Any>?
    }

    override fun initChildView() {
        setActionbar(CustomActionbar.TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT, "列表", null, 0, 0)
        enableRefresh(true)//是否可以下拉刷新，默认true
        enableLoad(true)//是否可以上拉加载，默认true
        setLoadingEnableCancel(false)//加载loading是否可以点击取消，默认false
        setEmptyMsgAndPic(
            getStringResId(R.string.common_no_data),
            R.mipmap.app_icon
        )//设置数据为空时页面显示的图标和文字
        //为list设置点击监听
        mBaseAdapter?.setOnItemClickListener(object : OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
               position.toString().showToast()
            }
        })
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(StationViewModel::class.java)
    }


    /**
     * 观察数据变化
     */
    override fun observer() {
        viewModel.listLiveData.observe(this) {
            val response = it.getOrNull()
            if (it.exceptionOrNull() != null) {
                //有异常，处理异常
                exceptionDispose(it.exceptionOrNull())
                return@observe
            }
            //处理正常流程
            response?.let {
                if (response.finished.list.isNullOrEmpty() && viewModel.dataList.isEmpty()) {
                    //数据为空，显示空页面提示
                    sendMessageToBase(REFRESH_EMPTY)
                    return@observe
                }
                if (response.finished.list.isNullOrEmpty() &&  viewModel.dataList.isNotEmpty()) {
                    //没有更多数据，显示页脚提示
                    sendMessageToBase(LOAD_FOOTER_NO_MORE_DATA)
                    return@observe
                }
                when (mRefreshLayout?.state) {
                    RefreshState.None, RefreshState.Refreshing -> {//刷新完成
                        sendMessageToBase(REFRESH_FIRST_LIST_VIEW)
                        viewModel.dataList.clear()
                        response.finished.list?.let { it1 ->
                            viewModel.dataList.addAll(it1)//将数据设置给viewModel
                            setCurrentAdapter(viewModel.dataList as MutableList<Any>)//将数据设置给adapter
                        }

                    }
                    RefreshState.Loading -> {//加载更多完成
                        sendMessageToBase(LOAD_FOOTER_LIST_VIEW)
                        response.finished.list?.let { it1 ->
                            viewModel.dataList.addAll(it1)
                            setCurrentAdapter(viewModel.dataList as MutableList<Any>)
                        }

                    }
                    else -> {
                    }
                }
            }
        }
    }

}