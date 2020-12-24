package com.jianyushanshe.androidstaging.common.holder

import android.graphics.Bitmap
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.SparseArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jianyushanshe.androidstaging.common.base.CommonBaseAdapter
import java.util.regex.Pattern

/**
 * Author:wangjianming
 * Description:ViewHolder
 */
class ViewHolder : RecyclerView.ViewHolder, View.OnClickListener {
    private var mViews: SparseArray<View?>
    private var mAdapters: SparseArray<CommonBaseAdapter<*>>
    var convertView: View
        private set
    private var mOnItemClickListener: CommonBaseAdapter.OnItemClickListener? = null
    private var mHeadersCount = 0

    constructor(convertView: View) : super(convertView) {
        mViews = SparseArray()
        mAdapters = SparseArray()
        this.convertView = convertView
    }

    constructor(
        convertView: View,
        onItemClickListener: CommonBaseAdapter.OnItemClickListener?,
        headersCount: Int
    ) : super(convertView) {
        mViews = SparseArray()
        mAdapters = SparseArray()
        this.convertView = convertView
        mOnItemClickListener = onItemClickListener
        mHeadersCount = headersCount
        convertView.setOnClickListener(this)
    }

    override fun onClick(view: View) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener?.onItemClick(view, layoutPosition - mHeadersCount)
        }
    }

    fun <C : CommonBaseAdapter<*>?> getAdapter(position: Int): C {
        return mAdapters[position] as C
    }

    /**
     * 功能：通过viewId获取控件
     */
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    /**
     * 功能：设置textView的text
     * param：viewId:textView的id，text:设置的文本
     */
    fun setText(viewId: Int, text: String?): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    /**
     * 功能：设置textView的text-关键词高亮显示
     * param：viewId:textView的id，text:设置的文本
     */
    fun setTextQuery(
        viewId: Int, text: String, query: String,
        color: Int
    ): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        setSearchKey(tv, text, query, color)
        return this
    }

    /**
     * 功能：设置textView的颜色
     */
    fun setTextColor(viewId: Int, text: String?, color: Int): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        tv.setTextColor(color)
        return this
    }

    fun setTextClick(
        viewId: Int, text: String?,
        onClickListener: View.OnClickListener?
    ): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        tv.setOnClickListener(onClickListener)
        return this
    }

    fun setLinClick(viewId: Int, onClickListener: View.OnClickListener?): ViewHolder {
        val ll = getView<LinearLayout>(viewId)!!
        ll.setOnClickListener(onClickListener)
        return this
    }

    fun setTextVisible(viewId: Int, visible: Boolean): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    /**
     * 功能：设置ImageView的图片
     * param：viewId:imageView的id，resId:图片id
     */
    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val iv = getView<ImageView>(viewId)!!
        iv.setImageResource(resId)
        return this
    }

    /**
     * 功能：设置ImageView的图片Bitmap
     * param：viewId:imageView的id，bm:图片Bitmap
     */
    fun setImageBitmap(viewId: Int, bm: Bitmap?): ViewHolder {
        val iv = getView<ImageView>(viewId)!!
        iv.setImageBitmap(bm)
        return this
    }

    fun setImageVisible(viewId: Int, visible: Boolean): ViewHolder {
        val iv = getView<ImageView>(viewId)!!
        iv.visibility = if (visible) View.VISIBLE else View.INVISIBLE
        return this
    }

    /**
     * 功能：设置ImageView的图片url
     * param：viewId:imageView的id，bm:图片Bitmap
     */
    fun setImageURL(viewId: Int, url: String?): ViewHolder {
        val iv = getView<ImageView>(viewId)!!
        return this
    }

    /**
     * @throws Exception
     * @功能：高亮显示搜索关键词
     * @param：textView:需要设置的textView searchKey:关键词 color:高亮显示的颜色 text:需要设置的句子
     * @return：
     */
    private fun setSearchKey(
        textView: TextView, text: String,
        searchKey: String, color: Int
    ) {
        val s = SpannableString(text)
        val p = Pattern.compile(searchKey)
        val m = p.matcher(s)
        while (m.find()) {
            val start = m.start()
            val end = m.end()
            s.setSpan(
                ForegroundColorSpan(color), start, end,
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
        textView.text = s
    }

}