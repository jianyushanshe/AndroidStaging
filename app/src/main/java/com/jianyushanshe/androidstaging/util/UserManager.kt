package com.jianyushanshe.androidstaging.util

import com.jianyushanshe.androidstaging.extension.edit
import com.jianyushanshe.androidstaging.logic.model.User

/**
 * Author:wangjianming
 * Description:用户管理类
 */
object UserManager {

    private const val USER_TOKEY = "user_token"
    private const val USER_ACCOUNT = "user_account"

    /**
     * 设置登录成功,保存用户信息
     */
    fun setLoginSuccess(user: User) {
        sharedPreferences.edit {
            putString(USER_TOKEY, user.token)
            putString(USER_ACCOUNT, user.phoneNum)
        }
    }

    /**
     * 获取token
     */
    fun getToken() = sharedPreferences.getString(USER_TOKEY, null)

    /**
     * 获取用户
     */
    fun getUser() = sharedPreferences.getString(USER_TOKEY, null)?.let {
        User(sharedPreferences.getString(USER_ACCOUNT, null), it)
    }


    /**
     * 注销登录
     */
    fun logout() {
        clearUserInfo()
    }

    /**
     * 清除用户信息
     */
    private fun clearUserInfo() {
        sharedPreferences.edit {
            putString(USER_TOKEY, null)
            putString(USER_ACCOUNT, null)
        }
    }

    private val lock = Any()

    /**
     * 判断是否登录
     */
    @Synchronized
    fun isLogin(): Boolean {
        synchronized(lock) {
            if (!sharedPreferences.getString(USER_TOKEY, null).isNullOrEmpty()) {
                return true
            }
        }
        return false
    }
}