package com.jianyushanshe.androidstaging.common.view

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.util.getScreenHeight
import com.jianyushanshe.androidstaging.util.getScreenWidth
import kotlinx.android.synthetic.main.dialog_confirm_0.*

/**
 * Author:wangjianming
 * Description:通用的dialog
 */
class DialogConfirm(context: Activity?) : AlertDialog(context), View.OnClickListener {

    private var mTip //提示语字符串
            : String? = null
    private var mTitle //标题字符串
            : String? = null
    private var mBtRightTx //取消按钮字符串
            : String? = null
    private var mBtLeftTx //确定按钮字符串
            : String? = null
    private var pic //图片
            : Drawable? = null
    private var spannableString //提示语字符串，某些字符设置不同颜色
            : SpannableString? = null
    private var mTipMaxLines //提示语最大行数
            = 0
    private var mTipGravity = Gravity.CENTER
    private var mOnBtClickListener //两个按钮的点击事件回调
            : OnBtClickListener? = null
    private var mOnOneBtClickListener //一个按钮的点击事件回调
            : OnOneBtClickListener? = null
    private var mLayoutId //自定义布局id
            = 0
    private var isOneButton //是否为单个按钮的弹窗 默认为两个按钮的弹窗
            = false
    private var isUseCurrentLayout = true //是否显示默认布局  默认为是

    //设置样式
    fun setLayoutStyle(setLayoutStyle: Int) {
        setlayoutStyle = setLayoutStyle
    }

    interface OnBtClickListener {
        fun OnBtOkClick(v: View?)
        fun OnBtCancelClick(v: View?)
    }

    interface OnOneBtClickListener {
        fun OnBtOkClick(v: View?)
    }

    fun setTvTipTx(tip: String?) {
        tv_tip?.text = tip
    }

    fun setTvTipTxAddOnClickListener(context: Context, tip: SpannableString?) {
        tv_tip?.highlightColor = context.resources.getColor(android.R.color.transparent)
        tv_tip?.text = tip
        tv_tip?.movementMethod = LinkMovementMethod.getInstance()
    }

    fun setTvTipTx(builder: SpannableStringBuilder?) {
        tv_tip?.text = builder
    }

    /**
     * 设置提示内容大小
     *
     * @param size
     */
    fun setTvTipTxSize(size: Float) {
        tv_tip?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    /**
     * 设置提示内容颜色
     *
     * @param color
     */
    fun setTvTipTxColor(color: Int) {
        tv_tip?.setTextColor(color)
    }

    /**
     * 设置提示内容显示位置
     *
     * @param
     */
    fun setTvTipTxPosition(position: Int) {
        tv_tip?.gravity = position
    }

    fun setBtnCancelTxColor(color: Int) {
        btn_cancel?.setTextColor(color)
    }

    /**
     * 设置标题颜色
     *
     * @param color
     */
    fun setmTvTitleColor(color: Int) {
        tv_title?.setTextColor(color)
    }

    /**
     * 设置标题文字大小
     *
     * @param size
     */
    fun setmTvTitleSize(size: Float) {
        tv_title?.setTextSize(TypedValue.COMPLEX_UNIT_SP, size)
    }

    fun setBtnOkTxColor(color: Int) {
        btn_sure?.setTextColor(color)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!isUseCurrentLayout && mLayoutId > 0) {
                setContentView(mLayoutId)
                btn_sure?.setOnClickListener(this)
                if (!isOneButton) btn_cancel?.setOnClickListener(this)

        } else {
            try {
                if (setlayoutStyle == LAYOUT_STYLE_O) {
                    setContentView(R.layout.dialog_confirm_0)
                } else if (setlayoutStyle == LAYOUT_STYLE_1) {
                    setContentView(R.layout.dialog_confirm_1)
                }
                if (!mTip.isNullOrEmpty()) {
                    tv_tip?.text = mTip
                }
                if (spannableString != null) {
                    tv_tip?.text = spannableString
                }
                if (!mTitle.isNullOrEmpty()) {
                    tv_title?.text = mTitle
                    tv_title?.visibility = View.VISIBLE
                }
                if (mTipMaxLines > 0) {
                    tv_tip?.maxLines = mTipMaxLines
                }
                if (pic != null) {
                    iv_pic_dialog.setImageDrawable(pic)
                    iv_pic_dialog.visibility = View.VISIBLE
                }
                tv_tip?.gravity = mTipGravity
                btn_sure?.setOnClickListener(this)
                if (!mBtRightTx.isNullOrEmpty()) {
                    btn_sure?.text = mBtRightTx
                }
                if (isOneButton) {
                    v_line?.visibility = View.GONE
                    btn_cancel?.visibility = View.GONE
                    if (setlayoutStyle == LAYOUT_STYLE_O) {
                        btn_sure?.setBackgroundResource(R.drawable.btn_dialog_bg_left_right_bottom)
                    } else if (setlayoutStyle == LAYOUT_STYLE_1) {
                        btn_sure?.setBackgroundResource(R.drawable.btn_submit_shape_blue_2)
                    }
                } else {
                    btn_cancel?.setOnClickListener(this)
                    if (!mBtLeftTx.isNullOrEmpty()) {
                        btn_cancel?.text = mBtLeftTx
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        val dialogWindow = window
        val p = dialogWindow?.attributes // 获取对话框当前的参数值
        p?.height = (getScreenHeight(context) * 0.9).toInt() // 高度设置为屏幕的0.6
        p?.width = (getScreenWidth(context) * 0.9).toInt() // 宽度设置为屏幕的0.65
        dialogWindow?.attributes = p
        dialogWindow?.setBackgroundDrawableResource(R.color.transparent)
        setCancelable(false)
        setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btn_cancel -> {
                dismiss()
                mOnBtClickListener?.OnBtCancelClick(v)
            }
            R.id.btn_sure -> {
                dismiss()
                mOnBtClickListener?.OnBtOkClick(v)
                mOnOneBtClickListener?.OnBtOkClick(v)
            }
            else -> {
            }
        }
    }


    companion object {
        var LAYOUT_STYLE_O = 0 //样式1
        var LAYOUT_STYLE_1 = 1 //样式2
        var setlayoutStyle = 0

        class Builder(context: Activity) {
            private val mDialog: DialogConfirm = DialogConfirm(context)

            /**
             * 设置样式
             *
             * @param layoutStyle 样式编号 0 ，1
             * @return
             */
            fun setLayoutStyle(layoutStyle: Int): Builder {
                setlayoutStyle = layoutStyle
                return this
            }

            /**
             * 设置为单个按钮的dialog
             *
             * @param maxLines 提示语最大行数
             */
            fun setTipMaxLines(maxLines: Int): Builder {
                mDialog.mTipMaxLines = maxLines
                return this
            }

            /**
             * 设置为单个按钮的dialog
             */
            fun setTipGravity(gravity: Int): Builder {
                mDialog.mTipGravity = gravity
                return this
            }

            /**
             * 设置dialog的标题
             */
            fun title(title: String?): Builder {
                mDialog.mTitle = title
                return this
            }

            /**
             * 设置dialog的图标
             *
             * @param pic
             */
            fun pic(pic: Drawable?): Builder {
                mDialog.pic = pic
                return this
            }

            /**
             * 设置dialog的提示语
             */
            fun tip(tip: String?): Builder {
                mDialog.mTip = tip
                return this
            }

            /**
             * 设置dialog的提示语,其中某些字体设置不同颜色
             */
            fun tipSpannableString(spannableString: SpannableString?): Builder {
                mDialog.spannableString = spannableString
                return this
            }

            /**
             * 设置dialog右边按钮的文字
             */
            fun btRightTx(btRightTx: String?): Builder {
                mDialog.mBtRightTx = btRightTx
                return this
            }

            /**
             * 设置dialog左边的文字
             */
            fun btLeftTx(btLeftTx: String?): Builder {
                mDialog.mBtLeftTx = btLeftTx
                return this
            }

            /**
             * 设置自定义布局id
             * 当前布局不能满足时，可以使用自己的布局（注：底部按钮左边的按钮id必须为 btn_cancel  右边按钮的id必须为 btn_sure 这样底部按钮才能正确回调）
             * 注意：只有在 isUseCurrentLayout 为false时生效
             *
             * @param layoutId 自定义布局id
             */
            fun setCustomLayout(layoutId: Int): Builder {
                mDialog.mLayoutId = layoutId
                mDialog.isUseCurrentLayout = false
                return this
            }

            /**
             * 设置为单个按钮的dialog
             *
             * @param btTx 按钮显示文字
             */
            fun setOneButtonDialog(btTx: String?): Builder {
                mDialog.isOneButton = true
                if (!TextUtils.isEmpty(btTx)) {
                    mDialog.mBtRightTx = btTx
                }
                return this
            }

            /**
             * dialog两个按钮的点击事件
             */
            fun onBtClickListener(onBtClickListener: OnBtClickListener?): Builder {
                mDialog.mOnBtClickListener = onBtClickListener
                return this
            }

            /**
             * dialog单个按钮的点击事件
             */
            fun onOneBtClickListener(onOneBtClickListener: OnOneBtClickListener?): Builder {
                mDialog.mOnOneBtClickListener = onOneBtClickListener
                return this
            }

            /**
             * 通过Builder类设置完属性后构造对话框的方法
             */
            fun create(): DialogConfirm {
                return mDialog
            }

        }
    }
}