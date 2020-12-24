package com.jianyushanshe.androidstaging.common.base

import androidx.lifecycle.ViewModel

/**
 * Author:wangjianming
 * Time:2020/12/9 16:13
 * Description:BaseViewModel
 */
abstract class BaseViewModel : ViewModel() {
    open fun getFirstData() {}
    open fun getNextData() {}
}