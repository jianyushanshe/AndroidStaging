package com.jianyushanshe.androidstaging.logic.dao

import com.jianyushanshe.androidstaging.logic.model.User
import com.jianyushanshe.androidstaging.util.UserManager

/**
 * Author:wangjianming
 * Time:2020/12/5 10:41
 * Description:MainDao
 */
object MainDao {
    fun cacheUser(user: User) {
        //存储用户信息到本地
        UserManager.setLoginSuccess(user)
    }

    fun getUser() = UserManager.getUser()
}