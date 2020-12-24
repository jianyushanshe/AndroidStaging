package com.jianyushanshe.androidstaging.extension

import android.widget.Toast
import com.jianyushanshe.androidstaging.MyApp

/**
 * Author:wangjianming
 * Time:2020/12/4 16:45
 * Description:CharSequence
 */
/**
 * 字符扩展函数，弹出Toast提示。
 *调用方法:  xxx.showToast()
 * @param duration 显示消息的时间  Either {@link #LENGTH_SHORT} or {@link #LENGTH_LONG}
 */
fun CharSequence.showToast(duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(MyApp.context, this, duration).show()
}
