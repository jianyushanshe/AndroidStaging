package com.jianyushanshe.androidstaging.common.view

import android.app.Dialog
import android.content.Context

/**
 * Author:wangjianming
 * Description:BaseDialog
 */
class BaseDialog(context: Context?, theme: Int, res: Int) : Dialog(
    context!!, theme
) {
    private val res: Int

    init {
        // TODO 自动生成的构造函数存根
        setContentView(res)
        this.res = res
        setCanceledOnTouchOutside(false)
    }
}