package com.jianyushanshe.androidstaging.common.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.collection.SparseArrayCompat
import androidx.recyclerview.widget.RecyclerView
import com.jianyushanshe.androidstaging.common.holder.ViewHolder

/**
 * Author:wangjianming
 * Description:CommonBaseAdapter recyclerVeiw的适配器
 */
 abstract class CommonBaseAdapter<T>(var mContext: Context) :
    RecyclerView.Adapter<ViewHolder>() {
    private var  mDataList: MutableList<T>? = mutableListOf()
    private val mHeaderViews = SparseArrayCompat<View?>() //header视图控件列表
    private val mFootViews = SparseArrayCompat<View?>() //foot视图控件列表
    private var mInflater: LayoutInflater? = null
    private var position = 0
    private var mOnItemClickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        mOnItemClickListener = listener
    }

   private fun isHeaderViewPos(position: Int): Boolean {
        return position < headersCount
    }

    private fun isFooterViewPos(position: Int): Boolean {
        return position >= headersCount + mDataList!!.size
    }

    fun addHeaderView(view: View?) {
        mHeaderViews.put(mHeaderViews.size() + BASE_ITEM_TYPE_HEADER, view)
    }

    fun addFootView(view: View?) {
        var isHave = false
        for (i in 0 until mFootViews.size()) {
            if (mFootViews.indexOfValue(view) == i) {
                isHave = true
                return
            }
        }
        if (!isHave) {
            mFootViews.put(mFootViews.size() + BASE_ITEM_TYPE_FOOTER, view)
        }
    }

    fun removeFootView(view: View?) {
        for (i in 0 until mFootViews.size()) {
            if (mFootViews.indexOfValue(view) == i) {
                mFootViews.removeAt(i)
            }
        }
    }

    fun removeHeaderView(view: View?) {
        for (i in 0 until mHeaderViews.size()) {
            if (mHeaderViews.indexOfValue(view) == i) {
                mHeaderViews.removeAt(i)
            }
        }
    }

    private val headersCount: Int
        get() = mHeaderViews.size()
    private val footersCount: Int
        get() = mFootViews.size()

    fun  update(mList: MutableList<T>?) {
        if (mDataList == null) {
            this.mDataList = mutableListOf()
        } else {
            this.mDataList?.clear()
            mList?.let { this.mDataList?.addAll(it) }
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return headersCount + footersCount + mDataList!!.size
    }

    val dataList: List<T>?
        get() = mDataList

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return when {
            mHeaderViews[viewType] != null -> {
                ViewHolder(
                    mHeaderViews[viewType]!!
                )
            }
            mFootViews[viewType] != null -> {
                ViewHolder(
                    mFootViews[viewType]!!
                )
            }
            else -> {
                var convertView = convertView
                if (convertView == null) {
                    convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false)
                }
                ViewHolder(
                    convertView!!, mOnItemClickListener, headersCount
                )
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderViewPos(position)) {
            return mHeaderViews.keyAt(position)
        } else if (isFooterViewPos(position)) {
            return mFootViews.keyAt(position - headersCount - mDataList!!.size)
        }
        return position - headersCount
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (isHeaderViewPos(position)) {
            return
        }
        if (isFooterViewPos(position)) {
            return
        }
        this.position = position
        conner(holder, mDataList!![position - headersCount])
    }

    /**
     * 功能：设置itemLayout的id
     */
    abstract val layoutId: Int
    private val convertView: View?
        get() = null

    abstract fun conner(holder: ViewHolder, entity: T)

    //define interface
    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    fun selectAll(isSelectAll: Boolean) {}

    companion object {
        private const val BASE_ITEM_TYPE_HEADER = 100000 //headerView标签
        private const val BASE_ITEM_TYPE_FOOTER = 200000 //footview标签
    }

    init {
        mInflater = LayoutInflater.from(mContext)
        if (mDataList == null) {
            this.mDataList = mutableListOf()
        }
    }
}