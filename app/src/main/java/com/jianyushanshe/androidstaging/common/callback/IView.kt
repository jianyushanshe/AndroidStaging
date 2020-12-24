package com.jianyushanshe.androidstaging.common.callback

import android.view.View

/**
 * Author:wangjianming
 * Time:2020/12/9 9:35
 * Description:RequestLifecycle
 */
interface IView {
    /**
     * 重新登录
     */
    fun reLogin(msg: String)

    /**
     * 显示网络状态
     */
    fun showNetWokrState(isConnect: Boolean)

    /**
     * 显示loading
     */
    fun showLoading()

    /**
     * 隐藏loading
     */
    fun dismissLoading()

    /**
     * 显示数据为空的页面
     */
    fun showEmptyView(imageRes: Int, msg: String)

    /**
     * 显示加载错误的页面
     */
    fun showLoadError()

    /**
     * 显示接口请求错误或数据加载错误的页面
     */
    fun showLoadError(onClickListener: View.OnClickListener)

    /**
     * 刷新视图
     */
    fun refreshView()

    /**
     * 显示自定义的view视图
     */
    fun showCustomView(view: View)

    /**
     * 显示加载的view
     */
    fun showMsgLoading(msg: String)

    fun setCurrentAdapter(list: MutableList<Any>?) //设置当前的listView的Adapter

    /**
     * 正常显示视图
     */
    fun showNormalView()

    /**
     * 向baseActivity的handler发送message
     */
    fun sendMessageToBase(what: Int)

}