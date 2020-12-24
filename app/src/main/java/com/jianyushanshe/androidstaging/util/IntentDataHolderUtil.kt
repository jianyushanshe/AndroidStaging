package com.jianyushanshe.androidstaging.util

import java.lang.Exception
import java.lang.ref.WeakReference

/**
 * Author:wangjianming
 * Time:2020/11/20 16:37
 * Description:IntentDataHolderUtil
 * 数据传输工具类，处理Intent携带大量数据时，超过1MB限制出现的异常场景。
 */
object IntentDataHolderUtil {
    private val dataList = hashMapOf<String, WeakReference<Any>>()

    fun setData(key: String, t: Any) {
        val value = WeakReference(t)
        dataList[key] = value
    }

    fun <T> getData(key: String): T? {
        val reference = dataList[key]
        return try {
            reference?.get() as T
        } catch (e: Exception) {
            null
        }
    }

}