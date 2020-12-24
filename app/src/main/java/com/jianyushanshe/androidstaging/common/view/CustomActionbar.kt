package com.jianyushanshe.androidstaging.common.view

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import com.jianyushanshe.androidstaging.R

/**
 * Author:wangjianming
 * Description:自定义的actionBar
 */
class CustomActionbar : LinearLayout {
    /**
     * actionbar 的 icon
     */
    private var icOfActionbar: View? = null

    /**
     * actionbar 中间的图片
     */
    private var ivOfActionbarCenter: ImageView? = null

    /**
     * actionbar中间的文字
     */
    private var tvOfActionbarCenter: TextView? = null

    /**
     * actionbar右侧的文字
     */
    private var tvOfActionbarRight: TextView? = null

    /**
     * actionbar右侧的图片
     */
    private var ivOfActionbarRight: ImageView? = null

    /**
     * actionbar左侧的返回按钮
     */
    private var ivOfActionbarLeftBack: ImageView? = null

    /**
     * 搜索时的进度加载条
     */
    private var pbSearchLoading: ProgressBar? = null

    /**
     * 中间的布局
     */
    private var rlCenterLayout: RelativeLayout? = null

    /**
     * 搜索输入编辑框
     */
    private var mEtSearch: EditText? = null

    /**
     * 文本清空按钮
     */
    private var ivClearSearchText: ImageView? = null

    /**
     * 搜索的view
     */
    private var layoutCustomSearch: View? = null

    /**
     * 上下文
     */
    private var myContext: Context? = null

    /**
     * actionbar标题
     */
    private var mTitle = "未知"

    /**
     * 搜索框布局
     */
    private var llSearchLayout: LinearLayout? = null

    /**
     * actionbar具有返回+搜索和条件选择功能
     */
    private var mActionbarOfSearchAndSwitch: View? = null

    /**
     * actionbar 具有返回+搜索功能
     */
    private var mActionbarOfSearch: View? = null

    /**
     * actionbar父容器
     */
    private var mActionbarContainer: FrameLayout? = null

    /**
     * 包含返回+搜索+条件选择的actionbar的布局
     */
    private var mContainerOfSearchAndSwitch: FrameLayout? = null

    /**
     * 包含搜索的actionbar的布局
     */
    private var mContainerOfSearch: FrameLayout? = null

    /**
     * 包含搜索+条件选择的actionbar的布局 的返回按钮
     */
    private var ivSearchAndSwichtBackLeft: ImageView? = null

    /**
     * 搜索编辑框,包含条件筛选的编辑框
     */
    private var mSearchEdit: EditText? = null

    /**
     * 包含搜索+条件选择的actionbar的布局 的搜索进度加载条
     */
    private var pbSearchAndSwitchLoading: ProgressBar? = null

    /**
     * 包含搜索+条件选择的actionbar的布局 的文本清除按钮
     */
    private var ivSearchAndSwitchTextClear: ImageView? = null


    /**
     * 包含搜索+条件选择的actionbar的布局 选中的条件文本
     */
    private var tvSerachAndSwitchSelectText: TextView? = null

    /**
     * 包含搜索框的布局
     */
    private var viewSearch: View? = null

    /**
     * 包含搜索的actionbar 右侧列表按钮
     */
    private var ivSearchOfRightList: ImageView? = null

    /**
     * 包含搜索的actionbar 的显示文本，只做显示，点击后跳转到搜索页面
     */
    private var tvSerachOfText: TextView? = null

    constructor(context: Context?) : super(context) {
        initActionbar(context)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        initActionbar(context)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initActionbar(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initActionbar(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
        ic_actionbar: View?
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        icOfActionbar = ic_actionbar
    }


    /**
     * @throws Exception
     * @功能：初始化actionbar的控件
     * @param：
     * @return：
     */
    private fun initActionbar(context: Context?) {
        myContext = context
        val mInflater = LayoutInflater.from(myContext)
        icOfActionbar = mInflater.inflate(R.layout.include_actionbar, null)
        addView(
            icOfActionbar, LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT
            )
        )
        mContainerOfSearchAndSwitch = findViewById(R.id.fl_actionbar_search_and_switch)
        mContainerOfSearch = findViewById(R.id.fl_actionbar_map_container)
        //父容器
        mActionbarContainer = findViewById(R.id.fl_actionbar_allBar)
        // 返回按钮
        ivOfActionbarLeftBack = findViewById(R.id.iv_actionbar_back_left)
        // 搜索加载
        pbSearchLoading = findViewById(R.id.pb_search_loadding)
        // 中间图标
        ivOfActionbarCenter = findViewById(R.id.iv_actionbar_center)
        // 中间文字
        tvOfActionbarCenter = findViewById(R.id.tv_actionbar_center)
        // 右侧文字
        tvOfActionbarRight = findViewById(R.id.tv_actionbar_right)
        // 右侧图片
        ivOfActionbarRight = findViewById(R.id.iv_actionbar_right)
        // 中间布局
        rlCenterLayout = findViewById(R.id.rlay_center_layout)
        //返回+搜索+条件选择的actionbar
        mActionbarOfSearchAndSwitch = findViewById(R.id.actionbar_search_and_switch)
        //返回+搜索的actionbar
        mActionbarOfSearch = findViewById(R.id.actionbar_search)
    }

    /**
     * 设置actionbar
     *
     * @param type
     * @param title
     * @param rightText
     * @param centerDrawble
     * @param rightDrawble
     */
    fun setActionbar(
        type: TYPE, title: String?, rightText: String?,
        centerDrawble: Int?, rightDrawble: Int?
    ) {
        var title = title
        initActionbar(myContext)
        if (title.isNullOrEmpty()) {
            title = ""
        } else {
            mTitle = title
        }
        goneAllWeiget()
        if (centerDrawble != NONE_PIC) {
            centerDrawble?.let { ivOfActionbarCenter?.setImageResource(it) }
        }
        if (rightDrawble != NONE_PIC) {
            rightDrawble?.let { ivOfActionbarRight?.setImageResource(it) }
        }
        mActionbarContainer?.visibility = VISIBLE
        when (type) {
            TYPE.ACTIONBAR_TYPE_OF_SEARCH_AND_SWITCH -> {
                mActionbarOfSearch?.visibility = GONE
                mActionbarOfSearchAndSwitch?.visibility = VISIBLE
                mActionbarContainer?.visibility = GONE
                mContainerOfSearchAndSwitch?.visibility = VISIBLE
                //左边的文字
                //左边的文字
                tvSerachAndSwitchSelectText = mActionbarOfSearchAndSwitch!!.findViewById(
                    R.id.tv_actionbar_left_local
                )
                ivSearchAndSwichtBackLeft =
                    mActionbarOfSearchAndSwitch!!.findViewById(R.id.iv_main_map)
                mSearchEdit = mActionbarOfSearchAndSwitch!!.findViewById(R.id.et_main_search)
                pbSearchAndSwitchLoading =
                    mActionbarOfSearchAndSwitch!!.findViewById(R.id.pb_main_search_loadding)
                ivSearchAndSwitchTextClear =
                    mActionbarOfSearchAndSwitch!!.findViewById(R.id.btn_main_clear_search_text)
            }
            TYPE.ACTIONBAR_TYPE_OF_MAP_SEARCH_RIGHTLIST -> {
                //搜索+右侧列表
                mActionbarOfSearch?.visibility = VISIBLE
                mActionbarOfSearchAndSwitch?.visibility = GONE
                mActionbarContainer?.visibility = GONE
                mContainerOfSearch?.visibility = VISIBLE
                ivSearchOfRightList =
                    mActionbarOfSearch!!.findViewById(R.id.iv_actionbar_map_list_of_right)
                tvSerachOfText = mActionbarOfSearch!!.findViewById(R.id.et_map_search)
            }
            TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT -> {
                ivOfActionbarLeftBack?.visibility = VISIBLE
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
                tvOfActionbarRight?.text = rightText
            }
            TYPE.ACTIONBAR_TYPE_OF_CENTER_PIC_RIGHTTEXT -> {
                ivOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarRight?.visibility = VISIBLE
                tvOfActionbarRight?.text = rightText
            }
            TYPE.ACTIONBAR_TYPE_OF_CENTERPIC_RIGHTPIC -> {
                ivOfActionbarCenter?.visibility = VISIBLE
                ivOfActionbarRight?.visibility = VISIBLE
            }
            TYPE.ACTIONBAR_TYPE_OF_CENTERPIC_FEE -> {
                ivOfActionbarLeftBack?.visibility = VISIBLE
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
                tvOfActionbarRight?.visibility = VISIBLE
                tvOfActionbarRight?.text = rightText
            }
            TYPE.ACTIONBAR_TYPE_OF_CENTERTEXT -> {
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
            }
            TYPE.ACTIONBAR_TYPE_OF_CENTERTEXT_RIGHT_TEXT -> {
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
                tvOfActionbarRight?.visibility = VISIBLE
                tvOfActionbarRight?.text = rightText
            }
            TYPE.ACTIONBAR_TYPE_OF_BACK_SEARCH -> {
                ivOfActionbarLeftBack!!.visibility = VISIBLE
                layoutCustomSearch = findViewById(R.id.layout_custom_search) // 搜索布局(外)
                // 搜索布局(里)
                llSearchLayout = findViewById(R.id.llay_search)
                mEtSearch = findViewById(R.id.et_search) // 返回按钮
                mEtSearch!!.requestFocus()
                ivClearSearchText = findViewById(R.id.btn_clear_search_text) // 清除按钮
                layoutCustomSearch?.visibility = VISIBLE
            }
            TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT_RIGHTPIC -> {
                ivOfActionbarLeftBack?.visibility = VISIBLE
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
                ivOfActionbarRight?.visibility = VISIBLE
            }
            TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT_RIGHT_TEXT -> {
                ivOfActionbarLeftBack?.visibility = VISIBLE
                tvOfActionbarCenter?.visibility = VISIBLE
                tvOfActionbarCenter?.text = title
                tvOfActionbarRight?.visibility = VISIBLE
                tvOfActionbarRight?.text = rightText
            }
            TYPE.ACTIONBAR_TYPE_OF_RIGHTPIC -> {
                ivOfActionbarLeftBack?.visibility = GONE
                tvOfActionbarCenter?.visibility = GONE
                tvOfActionbarRight?.visibility = GONE
                ivOfActionbarRight?.visibility = VISIBLE
            }
            TYPE.ACTIONBAR_TYPE_OF_TEXT_LOCALSEARCH_RIGHTPIC -> {
                tvSerachAndSwitchSelectText?.visibility = VISIBLE
                viewSearch?.visibility = VISIBLE
                ivOfActionbarRight?.visibility = VISIBLE
                viewSearch?.background?.alpha = 180
                val local = viewSearch?.findViewById<EditText>(R.id.et_search)
                local?.isClickable = false
                local?.clearFocus()
                local?.requestFocus()
                local?.setOnClickListener(null)
                local?.onFocusChangeListener = null
            }
        }
    }

    /**
     * 隐藏所有actionbar控件
     */
    private fun goneAllWeiget() {
        ivOfActionbarLeftBack?.visibility = GONE
        tvOfActionbarCenter?.visibility = GONE
        ivOfActionbarCenter?.visibility = GONE
        tvOfActionbarRight?.visibility = GONE
        ivOfActionbarRight?.visibility = GONE
    }

    /**
     * 获取包含搜素+条件选择的actionbar的文本清除按钮
     *
     * @return
     */
    fun getIvSearchAndSwitchTextClear() = ivSearchAndSwitchTextClear

    /**
     * 获取包含搜索的 actionbar的textview
     *
     * @return TextView
     */
    fun getSearchOfText()=tvSerachOfText

    /**
     * 获取包含搜索的 actionbar的右侧列表按钮
     *
     * @return ImageView
     */
    fun getSearchOfRightListIcon()=ivSearchOfRightList

    /**
     * 获取选择条件选择的文本
     *
     * @return TextView
     */
    fun getSwitchText()=tvSerachAndSwitchSelectText

    fun getClearImg()=ivSearchAndSwitchTextClear

    /**
     * 包含条件筛选的编辑框
     *
     * @return
     */
    fun getSearchEdit() = mSearchEdit

    fun getMapProgress()=pbSearchAndSwitchLoading

    fun getMapBackImg()=ivSearchAndSwichtBackLeft

    fun getIvClearSearchText()=ivClearSearchText

    fun getmEtSearch()=mEtSearch

    fun getmTitle()=mTitle

    /**
     * 获取actionbar右侧的图片
     *
     * @return
     */
    fun getIvOfActionbarRight()=ivOfActionbarRight

    fun getIvOfActionbarLeftBack()=ivOfActionbarLeftBack

    fun getRlCenterLayout()=rlCenterLayout

    fun getTvOfActionbarRight()=tvOfActionbarRight

    fun getLlSearchLayout()=llSearchLayout

    fun getPbSearchLoading()=pbSearchLoading

    companion object {
        const val NONE_PIC = 0
    }

    enum class TYPE {
        ACTIONBAR_TYPE_OF_SEARCH_AND_SWITCH,  //搜索+条件选择
        ACTIONBAR_TYPE_OF_BACK_CENTERTEXT,  // 返回键+文字+空
        ACTIONBAR_TYPE_OF_CENTER_PIC_RIGHTTEXT,  // 空+图片+文字
        ACTIONBAR_TYPE_OF_BACK_CENTERTEXT_RIGHT_TEXT,  // 返回键+文字+文字
        ACTIONBAR_TYPE_OF_CENTERPIC_FEE,  // 空+图片+空
        ACTIONBAR_TYPE_OF_CENTERPIC_RIGHTPIC,  // 空+图片+图片
        ACTIONBAR_TYPE_OF_CENTERTEXT,  // 空+文字+空
        ACTIONBAR_TYPE_OF_CENTERTEXT_RIGHT_TEXT,  // 空+文字+文字
        ACTIONBAR_TYPE_OF_BACK_SEARCH,  // 返回+搜索框
        ACTIONBAR_TYPE_OF_BACK_CENTERTEXT_RIGHTPIC,  // 返回键+文字+图片
        ACTIONBAR_TYPE_OF_TEXT_LOCALSEARCH_RIGHTPIC,  // 文字+本地搜索+图片
        ACTIONBAR_TYPE_OF_RIGHTPIC,  // 空+空+图片
        ACTIONBAR_TYPE_OF_MAP_SEARCH_RIGHTLIST
        //搜索框+右侧列表
    }
}

