package com.jianyushanshe.androidstaging.util

import android.app.Activity
import java.lang.ref.WeakReference
import java.util.*

/**
 * Author:wangjianming
 * Time:2020/11/17 15:38
 * Description:ActivityCollector
 */
object ActivityCollector {
    private val activitys = Stack<WeakReference<Activity>>()

    /**
     * 将activity压入栈
     */
    fun pushTask(task: WeakReference<Activity>?) {
        activitys.push(task)
    }

    /**
     * 将activity移除栈
     */
    fun removeTask(task: WeakReference<Activity>?) {
        activitys.remove(task)
    }

    /**
     * 从栈中移除指定位置的activity
     */
    fun removeTask(taskIndex: Int) {
        if (activitys.size > taskIndex) activitys.removeAt(taskIndex)
    }

    fun removeToTop() {
        val end = activitys.size
        val start = 1
        for (i in end - 1 downTo start) {
            val mActivity = activitys[i].get()
            if (null != mActivity && !mActivity.isFinishing) {
                mActivity.finish()
            }
        }
    }

    fun removeAll() {
        for (task in activitys) {
            val mActivity = task.get()
            if (null != mActivity && !mActivity.isFinishing) {
                mActivity.finish()
            }
        }
    }

}