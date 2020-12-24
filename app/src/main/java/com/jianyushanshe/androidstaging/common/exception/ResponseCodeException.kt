package com.jianyushanshe.androidstaging.common.exception

import java.lang.Exception

/**
 * Author:wangjianming
 * Time:2020/11/19 11:14
 * Description:ResponseCodeException
 */
class ResponseCodeException(val responseCode: Int, val msg: String?) : Exception()