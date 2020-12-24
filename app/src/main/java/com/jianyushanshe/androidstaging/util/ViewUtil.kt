package com.jianyushanshe.androidstaging.util

import android.app.Activity
import android.content.Context
import android.util.DisplayMetrics
import android.view.*
import android.view.ViewGroup.MarginLayoutParams
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.jianyushanshe.androidstaging.R
import java.lang.reflect.Method

/**
 * Author:wangjianming
 * Time:2020/12/9 14:28
 * Description:ViewUtil
 */

/**
 * Get center child in X Axes
 */
fun getCenterXChild(recyclerView: RecyclerView): View? {
    val childCount: Int = recyclerView.getChildCount()
    if (childCount > 0) {
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            if (isChildInCenterX(recyclerView, child)) {
                return child
            }
        }
    }
    return null
}

/**
 * Get position of center child in X Axes
 */
fun getCenterXChildPosition(recyclerView: RecyclerView): Int {
    val childCount: Int = recyclerView.getChildCount()
    if (childCount > 0) {
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            if (isChildInCenterX(recyclerView, child)) {
                return recyclerView.getChildAdapterPosition(child)
            }
        }
    }
    return childCount
}

/**
 * Get center child in Y Axes
 */
fun getCenterYChild(recyclerView: RecyclerView): View? {
    val childCount: Int = recyclerView.getChildCount()
    if (childCount > 0) {
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            if (isChildInCenterY(recyclerView, child)) {
                return child
            }
        }
    }
    return null
}

/**
 * Get position of center child in Y Axes
 */
fun getCenterYChildPosition(recyclerView: RecyclerView): Int {
    val childCount: Int = recyclerView.getChildCount()
    if (childCount > 0) {
        for (i in 0 until childCount) {
            val child: View = recyclerView.getChildAt(i)
            if (isChildInCenterY(recyclerView, child)) {
                return recyclerView.getChildAdapterPosition(child)
            }
        }
    }
    return childCount
}

fun isChildInCenterX(recyclerView: RecyclerView, view: View): Boolean {
    val childCount: Int = recyclerView.getChildCount()
    val lvLocationOnScreen = IntArray(2)
    val vLocationOnScreen = IntArray(2)
    recyclerView.getLocationOnScreen(lvLocationOnScreen)
    val middleX: Int = lvLocationOnScreen[0] + recyclerView.getWidth() / 2
    if (childCount > 0) {
        view.getLocationOnScreen(vLocationOnScreen)
        if (vLocationOnScreen[0] <= middleX && vLocationOnScreen[0] + view.width >= middleX) {
            return true
        }
    }
    return false
}

fun isChildInCenterY(recyclerView: RecyclerView, view: View): Boolean {
    val childCount: Int = recyclerView.getChildCount()
    val lvLocationOnScreen = IntArray(2)
    val vLocationOnScreen = IntArray(2)
    recyclerView.getLocationOnScreen(lvLocationOnScreen)
    val middleY: Int = lvLocationOnScreen[1] + recyclerView.getHeight() / 2
    if (childCount > 0) {
        view.getLocationOnScreen(vLocationOnScreen)
        if (vLocationOnScreen[1] <= middleY && vLocationOnScreen[1] + view.height >= middleY) {
            return true
        }
    }
    return false
}

fun setDialogAnim(view: View) {
    val aAnim = AlphaAnimation(1f, 0.0f)
    aAnim.duration = 300
    view.startAnimation(aAnim)
    aAnim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationStart(animation: Animation) {}
        override fun onAnimationEnd(animation: Animation) {
            view.visibility = View.GONE
        }

        override fun onAnimationRepeat(animation: Animation) {}
    })
}

/**
 * 获取加载的进度条
 *
 * @param context
 * @param msg
 * @return
 */
fun getLoadingView(context: Context?, msg: String?): View? {
    val proView: View = LayoutInflater.from(context).inflate(
        R.layout.page_loading, null
    )
    val progress = proView
        .findViewById<ImageView>(R.id.iv_progress)
    val options: RequestOptions = GlideOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
    GlideApp.with(context!!).asGif().load(R.mipmap.loading).apply(options).into(progress)
    return proView
}

/**
 * 隐藏软键盘 并且显示光标
 *
 * @param context
 * @param ed
 */
fun hideSoftInputShowCursor(context: Activity, ed: EditText) {
    context.window.setSoftInputMode(
        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
    )
    try {
        val cls = EditText::class.java
        val setShowSoftInputOnFocus: Method
        setShowSoftInputOnFocus = cls.getMethod(
            "setShowSoftInputOnFocus",
            Boolean::class.javaPrimitiveType
        )
        setShowSoftInputOnFocus.isAccessible = true
        setShowSoftInputOnFocus.invoke(ed, false)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * 隐藏软键盘
 *
 * @param context
 */
fun hideSoftInput(context: Activity) {
    context.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
}

/**
 * 隐藏软键盘
 *
 * @param context
 * @param edit
 */
fun hideSoftInput(context: Context, edit: View) {
    try {
        val inputManager = context
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(edit.windowToken, 0)
    } catch (e: Exception) {
    }
}

/**
 * @throws Exception
 * @功能：调出键盘
 * @param：
 * @return：
 */
fun showSoftInput(context: Context, eText: EditText?) {
    val imm = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    imm.showSoftInput(eText, InputMethodManager.SHOW_FORCED) // 隐藏键盘
}

/**
 * @throws Exception
 * @功能：键盘状态
 * @param：
 * @return：
 */
fun isKeyboardOpen(context: Context): Boolean {
    val imm = context.getSystemService(
        Context.INPUT_METHOD_SERVICE
    ) as InputMethodManager
    return imm.isActive
}

/**
 * 设置控件的高 参数:context 上下文 size;屏幕的倍数 view:需要调整的控件
 *
 * @param pxValue
 * @param view
 */
fun setViewsHeightPx(pxValue: Int, view: View) {
    val lp = view.layoutParams
    lp.height = pxValue
}

/**
 * 设置控件的宽 参数:context 上下文 size;屏幕的倍数 view:需要调整的控件
 *
 * @param pxValue
 * @param view
 */
fun setViewsWidthPx(pxValue: Int, view: View) {
    val lp = view.layoutParams
    lp.width = pxValue
}

/**
 * 获取屏幕宽
 */
fun getScreenWidth(context: Context): Int {
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(
        Context.WINDOW_SERVICE
    ) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

/**
 * 获取屏幕高
 */
fun getScreenHeight(context: Context): Int {
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(
        Context.WINDOW_SERVICE
    ) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.heightPixels
}

/**
 * 获取状态栏高度
 *
 * @param context context
 * @return 状态栏高度
 */
fun getStatusBarHeight(context: Context): Int {
    val mContext = context.applicationContext
    // 获得状态栏高度
    val resourceId = mContext.resources.getIdentifier("status_bar_height", "dimen", "android")
    return mContext.resources.getDimensionPixelSize(resourceId)
}

/**
 * 获取底部虚拟键盘的高度
 */
fun getBottomKeyboardHeight(context: Activity): Int {
    val screenHeight = getAccurateScreenDpi(context)[1]
    val dm = DisplayMetrics()
    context.windowManager.defaultDisplay.getMetrics(dm)
    return screenHeight - dm.heightPixels
}

/**
 * 获取精确的屏幕大小
 */
fun getAccurateScreenDpi(context: Activity): IntArray {
    val screenWH = IntArray(2)
    val display = context.windowManager.defaultDisplay
    val dm = DisplayMetrics()
    try {
        val c = Class.forName("android.view.Display")
        val method = c.getMethod("getRealMetrics", DisplayMetrics::class.java)
        method.invoke(display, dm)
        screenWH[0] = dm.widthPixels
        screenWH[1] = dm.heightPixels
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return screenWH
}

/**
 * 限制输入框输入的内容最多两位小数
 * 前提，输入框只能输入数字和小数点
 */
fun limitEditTxTwoDecimal(s: CharSequence, editText: EditText) {
    var s = s
    try {
        //这部分是处理如果输入框内小数点后有俩位，那么舍弃最后一位赋值，光标移动到最后
        if (s.toString().contains(".")) {
            if (s.length - 1 - s.toString().indexOf(".") > 2) {
                s = s.toString().subSequence(0, s.toString().indexOf(".") + 3)
                editText.setText(s)
                editText.setSelection(s.length)
            }
        }
        //这部分是处理如果用户输入以.开头，在前面加上0
        if (s.toString().trim { it <= ' ' }.substring(0) == ".") {
            s = "0$s"
            editText.setText(s)
            editText.setSelection(2)
        }
        //这里处理用户 多次输入.的处理 比如输入 1..6的形式，是不可以的
        if (s.toString().trim { it <= ' ' }.length > 1) {
            if (s.toString().substring(
                    s.toString().trim { it <= ' ' }.length - 1,
                    s.toString().trim { it <= ' ' }.length
                ) == "."
            ) { //判断最后一位是否是小数点
                val r = s.toString().substring(0, s.toString().trim { it <= ' ' }.length - 1)
                if (r.indexOf(".") > 0) {
                    editText.setText(s.subSequence(0, s.toString().trim { it <= ' ' }.length - 1))
                    editText.setSelection(editText.length())
                }
                return
            }
        }
    } catch (throwable: Throwable) {
    }
}

fun setMargins(v: View, l: Int, t: Int, r: Int, b: Int) {
    if (v.layoutParams is MarginLayoutParams) {
        val p = v.layoutParams as MarginLayoutParams
        p.setMargins(l, t, r, b)
        v.requestLayout()
    }
}