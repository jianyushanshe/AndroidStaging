package com.jianyushanshe.androidstaging.common.view

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.jianyushanshe.androidstaging.R

/**
 * Author:wangjianming
 * Description:一个方便在多种状态切换的view
 */
class MultipleStatusView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private var mEmptyView: View? = null
    private var mErrorView: View? = null
    private var mContentView: View? = null
    private var mErrorRetryView: View? = null
    private val mEmptyViewResId: Int
    private val mErrorViewResId: Int

    /**
     * 获取当前状态
     */
    private var viewStatus = 0
    private var mInflater: LayoutInflater? = null
    private var mOnRetryClickListener: OnClickListener? = null
    private val mLayoutParams = ViewGroup.LayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        mInflater = LayoutInflater.from(context)
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    fun setOnRetryClickListener(onRetryClickListener: OnClickListener?) {
        mOnRetryClickListener = onRetryClickListener
    }

    /**
     * 显示空视图
     */
    fun showEmpty(imageRes: Int, emptyMsg: String?) {
        viewStatus = STATUS_EMPTY
        if (null == mEmptyView) {
            mEmptyView = mInflater?.inflate(mEmptyViewResId, null)
            val icon = mEmptyView?.findViewById<ImageView>(R.id.iv_no_data)
            if (imageRes != 0) {
                icon?.setImageResource(imageRes)
            }
            val textView = mEmptyView?.findViewById<TextView>(R.id.tv_no_data)
            if (!TextUtils.isEmpty(emptyMsg)) {
                textView?.text = emptyMsg
            }
            scaleViewAnimator(icon!!, 1000, 0)
            scaleViewAnimator(textView!!, 1000, 0)
            mEmptyView?.isClickable = true
            addView(mEmptyView, 0, mLayoutParams)
        }
        showViewByStatus(viewStatus)
    }

    /**
     * 显示错误视图
     */
    fun showError() {
        viewStatus = STATUS_ERROR
        if (null == mErrorView) {
            mErrorView = mInflater?.inflate(mErrorViewResId, null)
            mErrorRetryView = mErrorView?.findViewById(R.id.iv_page_load_fail)
            if (null != mOnRetryClickListener && null != mErrorRetryView) {
                mErrorRetryView?.setOnClickListener(mOnRetryClickListener)
            }
            scaleViewAnimator(mErrorRetryView!!, 1000, 0)
            addView(mErrorView, 0, mLayoutParams)
        }
        showViewByStatus(viewStatus)
    }

    /**
     * 显示自定义内容视图
     */
    fun showCustomContent(contentView: View?) {
        viewStatus = STATUS_CONTENT
        if (null == mContentView) {
            mContentView = contentView
            addView(mContentView, 0, mLayoutParams)
        }
        showViewByStatus(viewStatus)
    }

    private fun showViewByStatus(viewStatus: Int) {
        mEmptyView?.visibility = if (viewStatus == STATUS_EMPTY) VISIBLE else GONE
        mErrorView?.visibility = if (viewStatus == STATUS_ERROR) VISIBLE else GONE
        mContentView?.visibility = if (viewStatus == STATUS_CONTENT) VISIBLE else GONE
    }

    companion object {
        const val STATUS_CONTENT = 0x00
        const val STATUS_LOADING = 0x01
        const val STATUS_EMPTY = 0x02
        const val STATUS_ERROR = 0x03
        const val STATUS_NO_NETWORK = 0x04
    }

    init {
        val a =
            context.obtainStyledAttributes(attrs, R.styleable.MultipleStatusView, defStyleAttr, 0)
        mEmptyViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_emptyView, R.layout.page_no_data)
        mErrorViewResId =
            a.getResourceId(R.styleable.MultipleStatusView_errorView, R.layout.page_load_fail)
        a.recycle()
    }

    /**
     * View缩放动画
     *
     * @param view
     * @param time
     * @param count
     */
    private fun scaleViewAnimator(view: View, time: Int, count: Int) {
        val scaleAnimation = ScaleAnimation(
            0f,
            1f,
            0f,
            1f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        scaleAnimation.duration = time.toLong()
        scaleAnimation.repeatCount = count
        scaleAnimation.fillAfter = true
        view.startAnimation(scaleAnimation)
    }
}