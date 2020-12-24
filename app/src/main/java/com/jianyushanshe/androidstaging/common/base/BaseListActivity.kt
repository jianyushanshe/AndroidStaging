package com.jianyushanshe.androidstaging.common.base

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.event.*
import com.jianyushanshe.androidstaging.util.Constants.LOAD_FOOTER_LIST_VIEW
import com.jianyushanshe.androidstaging.util.Constants.LOAD_FOOTER_NO_MORE_DATA
import com.jianyushanshe.androidstaging.util.Constants.REFRESH_EMPTY
import com.jianyushanshe.androidstaging.util.Constants.REFRESH_FIRST_LIST_VIEW
import com.jianyushanshe.androidstaging.util.getStringResId
import com.jianyushanshe.androidstaging.util.isNetConnected
import com.jianyushanshe.androidstaging.util.setDialogAnim
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * BaseListActivity
 * 列表基类，所有展示列表的页面继承该类
 */
abstract class BaseListActivity<T : BaseViewModel> : BaseActivity<T>() {
    var mRecyclerView: RecyclerView? = null
    var mBaseAdapter: CommonBaseAdapter<Any>? = null
    var mRefreshLayout: SmartRefreshLayout? = null
    var mDrawerLayout: RelativeLayout? = null

    /**
     * ListView没有更多数据时的footer
     */
    protected var mListViewLoadFooter: View? = null

    /**
     * 没有更多数据时的View
     */
    private var mLlNoneData: LinearLayout? = null
    private var mTvNoneMoreData: TextView? = null
    private var mTvNoneMoreDataTip: TextView? = null
    var isShowTvNoneMoreData =
        false//是否显示没有更多数据的页脚，要显示页脚提示文字，需要在activity中初始化该变量为isShowTvNoneMoreData=ture
    var isShowTvNoneMoreDataTip = false//是否显示页脚的说明文字
    var mIsEnableRefresh = true //是否可以下拉刷新
    var mIsEnableLoad = true //是否可以上拉加载更多
    var mIsExpandableList = false //是否是ExpandableList（列表中有子列表）
    var mIsReverse = false //是否反向 （下拉加载更多，上拉刷新）

    /**
     * 数据为空的文字显示
     */
    private var emptyText: String = getStringResId(R.string.common_no_data)

    /**
     * 数据为空的图片显示
     */
    private var emptyPic: Int = R.mipmap.empty_data

    /**
     * 默认的布局文件
     */
    var defaultLayoutId: Int = R.layout.activity_base_list

    /**
     * handler处理数据加载或刷新后的页面状态
     */
    private var mBaseHandler: Handler = @SuppressLint("HandlerLeak")
    object : Handler() {
        override fun handleMessage(msg: Message) {
            val what = msg.what
            if (mIsEnableRefresh) mRefreshLayout?.setEnableRefresh(true)
            if (mRlBase.isVisible) setDialogAnim(mRlBase)
            when (what) {
                REFRESH_EMPTY -> {
                    showEmptyView(emptyPic,emptyText)
                    mRefreshLayout?.let {
                        if (it.isRefreshing) it.finishRefresh(true)
                        if (it.isLoading) it.finishLoadMore(true)
                        it.setEnableLoadMore(false)
                    }
                    mBaseAdapter?.let {
                        it.removeFootView(mListViewLoadFooter)
                    }
                }
                REFRESH_FIRST_LIST_VIEW -> {
                    enableLoad(true)
                    mRefreshLayout?.let {
                        if (it.isRefreshing) it.finishRefresh(true)
                        if (mIsEnableLoad) it.setEnableLoadMore(true)
                    }
                    mBaseAdapter?.let {
                        it.removeFootView(mListViewLoadFooter)
                        it.notifyDataSetChanged()
                    }
                }
                LOAD_FOOTER_NO_MORE_DATA -> {
                    enableLoad(false)
                    mRefreshLayout?.let {
                        if (it.isLoading) it.finishLoadMore(true)
                        if (it.isRefreshing) it.finishRefresh(true)
                        if (isShowTvNoneMoreData) mBaseAdapter?.addFootView(mListViewLoadFooter)
                        mBaseAdapter?.notifyDataSetChanged()
                        it.finishLoadMore(false)
                    }
                }
                LOAD_FOOTER_LIST_VIEW -> {
                    mRefreshLayout?.let {
                        if (it.isLoading) it.finishLoadMore(true)
                        if (mIsEnableLoad) it.setEnableLoadMore(true)
                    }
                    mBaseAdapter?.let {
                        it.removeFootView(mListViewLoadFooter)
                        it.notifyDataSetChanged()
                    }
                }
            }
        }
    }

    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        initData()
        initView()
    }

    /**
     * 设置数据为空页面的图标和文字
     */
    open fun setEmptyMsgAndPic(msg: String, pic: Int) {
        emptyText = msg
        emptyPic = pic
    }

    /**
     * 初始化数据
     */
    open fun initData() {}

    /**
     * 初始化view
     */
    @SuppressLint("InflateParams")
    open fun initView() {
        setContentView(defaultLayoutId)
        mRecyclerView = findViewById(R.id.public_rv)
        mRecyclerView?.layoutManager = LinearLayoutManager(activity)
        mRefreshLayout = findViewById(R.id.refreshLayout)
        mDrawerLayout = findViewById(R.id.drawer_layout)
        mMultipleStatusView = findViewById(R.id.multiple_status_view)
        initAdapter()
        mRecyclerView?.adapter = mBaseAdapter
        observer()
        //设置 Header
        setRefreshHeader(mRefreshLayout)
        //设置 Footer
        setRefreshFooter(mRefreshLayout)
        mRefreshLayout?.setOnRefreshListener { getFirstData() }
        mRefreshLayout?.setOnLoadMoreListener { getNextData() }
        mListViewLoadFooter = LayoutInflater.from(activity).inflate(
            R.layout.include_swipe_listview_footer,
            null,
            false
        )
        val lp = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        mListViewLoadFooter?.layoutParams = lp
        mLlNoneData = mListViewLoadFooter?.findViewById(R.id.ll_none_data)
        mTvNoneMoreData = mListViewLoadFooter?.findViewById(R.id.tv_none_data)
        if (isShowTvNoneMoreDataTip) {
            mTvNoneMoreDataTip = mListViewLoadFooter?.findViewById(R.id.tv_none_data_tip)
            mTvNoneMoreDataTip?.visibility = View.VISIBLE
        }
        initChildView()
    }

    /**
     * 观察数据
     */
    abstract fun observer()

    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMessageEvent(messageEvent: MessageEvent) {
        when (messageEvent) {
            is NetExceptionEvent -> showLoadError()//网络相关的异常统一处理
            is ReLoginEvent -> reLogin(messageEvent.msg)//重新登录
            is ShowLoadingEvent -> showLoading()//显示加载loading
            is DismissLoadingEvent -> dismissLoading()//隐藏loading
        }
    }

    /**
     * 发消息给父类处理
     *
     * @param what 消息
     */
    override fun sendMessageToBase(what: Int) {
        mBaseHandler.sendEmptyMessage(what)
    }

    /**
     * 获取下一页数据
     */
    open fun getNextData() {
        viewModel.getNextData()
    }

    /**
     * 获取第一页数据
     */
    open fun getFirstData() {
        if (!isNetConnected(this)) {
            showLoadError()
            return
        }
        viewModel.getFirstData()
    }

    open fun setTvNoneMoreDataTx(tx: String) {
        mTvNoneMoreData?.text = tx
    }

    open fun setTvNoneMoreDataTipTx(tx: String) {
        mTvNoneMoreDataTip?.text = tx
    }

    open fun setLlNoneDataVisible(visible: Int) {
        mLlNoneData?.visibility = visible
    }

    /**
     * 功能：设置刷新功能
     */
    open fun enableRefresh(flag: Boolean) {
        mIsEnableRefresh = flag
        mRefreshLayout?.setEnableRefresh(flag)
    }

    /**
     * 功能：设置加载更多功能
     */
    open fun enableLoad(flag: Boolean) {
        mIsEnableLoad = flag
        mRefreshLayout?.setEnableLoadMore(flag)
    }

    /**
     * 是否显示列表无数据的页脚
     *
     * @param flag
     */
    open fun listShowNoneDataFooter(flag: Boolean) {
        isShowTvNoneMoreData = flag
        isShowTvNoneMoreDataTip = flag
    }


    /**
     * 初始化adapter
     */
    abstract fun initAdapter()

    /**
     * 初始化子类里的布局
     */
    abstract fun initChildView()

    override fun  setCurrentAdapter(list: MutableList<Any>?) {
        super.setCurrentAdapter(list)
        refreshView()
        mBaseAdapter?.update(list)
    }
}