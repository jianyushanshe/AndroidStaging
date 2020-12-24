package com.jianyushanshe.androidstaging.common.base

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.gyf.immersionbar.ImmersionBar
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.event.*
import com.jianyushanshe.androidstaging.extension.logD
import com.jianyushanshe.androidstaging.extension.logW
import com.jianyushanshe.androidstaging.extension.showToast
import com.jianyushanshe.androidstaging.common.callback.IView
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.common.view.DialogConfirm
import com.jianyushanshe.androidstaging.common.view.MultipleStatusView
import com.jianyushanshe.androidstaging.ui.home.MainActivity
import com.jianyushanshe.androidstaging.util.*
import com.jianyushanshe.androidstaging.util.UserManager.logout
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.api.RefreshLayout
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.lang.ref.WeakReference

/**
 * Author:wangjianming
 * Time:2020/12/9 10:30
 * Description:BaseActivity
 */
abstract class BaseActivity<T : BaseViewModel> : AppCompatActivity(), IView {
    /**
     * viewModel
     */
    lateinit var viewModel: T

    /**
     * 内容布局
     */
    var mLayoutContent: FrameLayout? = null

    /**
     * 中间布局
     */
    lateinit var mRlBase: RelativeLayout

    /**
     * 全局布局
     */
    var mLlRootView: LinearLayout? = null

    var llActionBar: LinearLayout? = null

    /**
     * 判断当前activity是否在前台
     */
    var isActive: Boolean = false

    /**
     * 当前Activity
     */
    var activity: Activity? = null

    /**
     * 当前Activity的弱引用，防止内存泄露
     */
    private var activityWR: WeakReference<Activity>? = null

    /**
     * 日志输出标志
     */
    private val TAG_LOG: String = this.javaClass.simpleName

    /**
     * 登录被踢下线提示dialog
     */
    private var mDialog: DialogConfirm? = null

    /**
     * actionbar
     */
    private var mActionbar: CustomActionbar? = null

    /**
     * 多状态切换view
     */
    var mMultipleStatusView: MultipleStatusView? = null

    /**
     * 进度加载loading
     */
    private var mProgressBar: Dialog? = null

    /**
     * 是否点击可取消显示dialog
     */
    private var isEnableCancel: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        super.onCreate(savedInstanceState)
        super.setContentView(R.layout.activity_base)
        logD(TAG_LOG, "BaseActivity-->onCreate()")
        activity = this
        activityWR = WeakReference(activity!!)
        ActivityCollector.pushTask(activityWR)
        EventBus.getDefault().register(this)
        initViewModel()//初始化viewModel
        superBaseInitView() //初始化顶级父类的布局
        superBaseOnCreate(savedInstanceState) //父类暴露给自己的初始化方法，在子类中重写
        initLoading() //初始化loading
        requestData() //请求数据
    }

    /**
     * 顶层父类初始化view
     */
    private fun superBaseInitView() {
        mRlBase = findViewById(R.id.rl_base)
        mActionbar = findViewById(R.id.ic_actionbar)
        llActionBar = findViewById(R.id.ll_action_bar)
        mLlRootView = findViewById(R.id.ll_root_view)
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        setStatusBarBackgroud(R.color.colorPrimaryDark)
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        setStatusBarBackgroud(R.color.colorPrimaryDark)
    }

    /**
     * 设置状态栏背景色
     * statusBarColor 状态栏的颜色，根据页面的需求给状态栏设置对应的颜色，状态栏文字会对应进行变化
     * 如果要实现沉浸式，在activity中重写该方法
     */
    open fun setStatusBarBackgroud(@ColorRes statusBarColor: Int) {
        //沉浸式
        //ImmersionBar.with(this).init()
        //设置有工具栏
        ImmersionBar.with(this).autoStatusBarDarkModeEnable(true, 0.2f)
            .statusBarColor(statusBarColor).fitsSystemWindows(true).init()
    }

    abstract fun initViewModel()

    /**
     * onCreate中请求当前页面所需数据
     */
    private fun requestData() {

    }

    /**
     * onResume中请求数据
     */
    open fun requestDataOnResume() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    open fun onMessageEvent(messageEvent: MessageEvent) {
        when (messageEvent) {
            is NetExceptionEvent -> messageEvent.msg.showToast()//网络相关的异常统一处理
            is ReLoginEvent -> reLogin(messageEvent.msg)//重新登录
            is ShowLoadingEvent -> showLoading()//显示加载loading
            is DismissLoadingEvent -> dismissLoading()//隐藏loading
        }
    }

    /**
     * 父类暴露给自己初始化的方法
     *
     * @param savedInstanceState Bundle
     */
    abstract fun superBaseOnCreate(savedInstanceState: Bundle?)

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        logD(TAG_LOG, "BaseActivity-->onSaveInstanceState()")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        logD(TAG_LOG, "BaseActivity-->onRestoreInstanceState()")
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        logD(TAG_LOG, "BaseActivity-->onNewIntent()")
    }

    override fun onRestart() {
        super.onRestart()
        logD(TAG_LOG, "BaseActivity-->onRestart()")
    }

    override fun onStart() {
        super.onStart()
        logD(TAG_LOG, "BaseActivity-->onStart()")
    }

    override fun onResume() {
        super.onResume()
        logD(TAG_LOG, "BaseActivity-->onResume()")
        isActive = true
        if (isNetConnected(applicationContext)) {
            requestDataOnResume()
        } else {
            R.string.net_work_state_error.showToast()
        }
    }

    override fun onPause() {
        super.onPause()
        logD(TAG_LOG, "BaseActivity-->onPause()")
        isActive = false
    }

    override fun onStop() {
        super.onStop()
        logD(TAG_LOG, "BaseActivity-->onStop()")
    }

    override fun onDestroy() {
        super.onDestroy()
        logD(TAG_LOG, "BaseActivity-->onDestroy()")
        if (mDialog?.isShowing == true) mDialog?.dismiss()
        if (mProgressBar?.isShowing == true) mProgressBar?.dismiss()
        activity = null
        ActivityCollector.removeTask(activityWR)
        EventBus.getDefault().unregister(this)
    }

    /**
     * 重新登录
     */
    override fun reLogin(msg: String) {
        logout()
        showReloginDialog(msg)
    }

    override fun showNetWokrState(isConnect: Boolean) {
        if (!isConnect) R.string.net_work_state_error.showToast()
    }

    /**
     * 初始化loading
     */
    private fun initLoading() {
        if (activity?.isFinishing == true) return
        if (mProgressBar?.isShowing == true) mProgressBar?.setCancelable(isEnableCancel)
    }


    /**
     * 显示加载中loading
     */
    override fun showLoading() {
        activity?.let {
            if (mProgressBar == null) {
                mProgressBar = Dialog(it, R.style.CustomProgressDialog)
                getLoadingView(it, null)?.let { view ->
                    mProgressBar?.setContentView(
                        view, LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                } // 设置布局
            }
            mProgressBar?.let { progressBar ->
                progressBar.setCancelable(isEnableCancel)
                if (!progressBar.isShowing) progressBar.show()
            }
        }
    }


    /**
     * 显示数据为空的页面
     */
    override fun showEmptyView(imageRes: Int, msg: String) {
        mMultipleStatusView?.let {
            if (it.visibility == View.GONE) it.visibility = View.VISIBLE
            it.showEmpty(imageRes, msg)
        }
    }

    /**
     * 显示接口请求错误或数据加载错误的页面
     */
    override fun showLoadError() {
        mMultipleStatusView?.let {
            if (it.visibility == View.GONE) it.visibility = View.VISIBLE
            mMultipleStatusView?.setOnRetryClickListener {
                requestData()
                requestDataOnResume()
            }
            mMultipleStatusView?.showError()
        }
    }

    /**
     * 显示接口请求错误或数据加载错误的页面
     */
    override fun showLoadError(onClickListener: View.OnClickListener) {
        mMultipleStatusView?.let {
            if (it.visibility == View.GONE) it.visibility = View.VISIBLE
            it.showError()
            it.setOnRetryClickListener(onClickListener)
        }
    }

    override fun refreshView() {

    }

    /**
     * 显示自定义内容视图
     */
    override fun showCustomView(view: View) {
        mMultipleStatusView?.let {
            if (it.visibility == View.GONE) it.visibility = View.VISIBLE
            it.showCustomContent(view)
        }
    }

    /**
     * 隐藏加载loading
     */
    override fun dismissLoading() {
        mProgressBar?.dismiss()
        mProgressBar = null
    }

    /**
     * 显示加载的view
     */
    override fun showMsgLoading(msg: String) {
        activity?.let {
            try {
                if (mProgressBar == null) mProgressBar = Dialog(it, R.style.CustomProgressDialog)
                getLoadingView(it, msg)?.let { view ->
                    mProgressBar?.setContentView(
                        view, LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT
                        )
                    )
                } // 设置布局
                mProgressBar?.let { progressBar ->
                    progressBar.setCancelable(isEnableCancel)
                    if (!progressBar.isShowing) progressBar.show()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun setCurrentAdapter(list: MutableList<Any>?) {
        if (list?.isNotEmpty() == true && mMultipleStatusView?.isVisible == true) {
            mMultipleStatusView?.visibility = View.GONE
        }
    }

    /**
     * 隐藏异常页面，显示正常视图
     */
    override fun showNormalView() {
        mMultipleStatusView?.visibility = View.GONE
    }


    /**
     * 设置loading是否可以取消
     *
     * @param enableCancel
     */
    fun setLoadingEnableCancel(enableCancel: Boolean) {
        isEnableCancel = enableCancel
    }

    /**
     * 设置actionbar
     */
    fun setActionbar(
        type: CustomActionbar.TYPE,
        title: String?,
        rightText: String?,
        centerDrawable: Int?,
        rightDrawable: Int?
    ) {
        if (mActionbar == null) {
            mActionbar = findViewById(R.id.ic_actionbar)
            mActionbar?.let {
                //acitonbar设置距离顶部的margin
//                val layoutParams = it.layoutParams as LinearLayout.LayoutParams
//                layoutParams.setMargins(0, getStatusBarHeight(activity!!), 0, 0)
//                it.layoutParams = layoutParams
                it.visibility = View.VISIBLE
                it.getIvOfActionbarLeftBack()?.setOnClickListener { finish() }
            }
        }
        mActionbar?.setActionbar(type, title, rightText, centerDrawable, rightDrawable)
    }


    /**************************强制退出登录的提示dialog部分  */
    /**
     * 是否展示了被挤下线弹登录提示的对话框
     */
    var isLoginDialog: Boolean = false
    private fun showReloginDialog(msg: String) {
        logW(TAG_LOG, "强制退出登录提示dialog")
        if (!isLoginDialog) {
            mDialog = mDialog ?: activity?.let {
                DialogConfirm.Companion.Builder(it)
                    .setLayoutStyle(DialogConfirm.LAYOUT_STYLE_O)
                    .tip(msg)
                    .btRightTx(getStringResId(R.string.login))
                    .btLeftTx(getStringResId(R.string.cancel))
                    .onBtClickListener(object : DialogConfirm.OnBtClickListener {
                        override fun OnBtOkClick(v: View?) {
                            openActivity<MainActivity>(activity!!)
                            isLoginDialog = false
                        }

                        override fun OnBtCancelClick(v: View?) {
                            isLoginDialog = false
                        }
                    }).create()
            }

            if (activity?.isFinishing == false) {
                mDialog?.show()
                isLoginDialog = true
            }
        }
    }


    /**************************下拉刷新上拉加载的样式设置部分 ******************************/
    /**************************下拉刷新上拉加载的样式设置部分  */
    /**
     * 设置下拉刷新的HeaderView
     */
    open fun setRefreshHeader(refreshLayout: RefreshLayout?) {
        //设置 Header
        refreshLayout?.setRefreshHeader(
            ClassicsHeader(activity)
        )
    }

    /**
     * 设置上拉加载的FooterView
     */
    open fun setRefreshFooter(refreshLayout: RefreshLayout?) {
        //设置 Footer
        refreshLayout?.setRefreshFooter(
            ClassicsFooter(activity)
        )
    }

    /**
     * 发送消息给父类handler进行处理
     *
     * @param what 消息
     */
    override fun sendMessageToBase(what: Int) {

    }

}