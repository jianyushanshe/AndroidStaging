package com.jianyushanshe.androidstaging.extension

import android.content.SharedPreferences

/**
 * Author:wangjianming
 * Time:2020/11/17 16:54
 * Description:SharedPreferences
 */

fun SharedPreferences.edit(action: SharedPreferences.Editor.() -> Unit) {
    val editor = edit()
    action(editor)
    editor.apply()
}