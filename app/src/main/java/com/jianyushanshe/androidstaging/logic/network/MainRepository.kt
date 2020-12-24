package com.jianyushanshe.androidstaging.logic.network

import androidx.lifecycle.liveData
import com.jianyushanshe.androidstaging.logic.dao.MainDao
import com.jianyushanshe.androidstaging.logic.dto.LoginRequest
import com.jianyushanshe.androidstaging.logic.dto.VerificationCodeRequest
import com.jianyushanshe.androidstaging.logic.dto.WorkOrderRequest
import com.jianyushanshe.androidstaging.logic.model.User
import com.jianyushanshe.androidstaging.common.exception.ResponseCodeException
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

/**
 * Author:wangjianming
 * Time:2020/12/5 10:24
 * Description:MainRepository
 */
object MainRepository {
    /**
     * 缓存用户数据到本地
     */
    fun cacheUser(userEntity: User) = MainDao.cacheUser(userEntity)

    /**
     * 本地获取用户数据
     */
    fun getUser() = MainDao.getUser()

    /**
     * 登录
     */
    fun login(request: LoginRequest) = fire(Dispatchers.IO) {
        val response = NetWork.login(request)
        if (response.code == SUCCESS) {
            val user = response.data
            Result.success(user)
        } else {
            Result.failure(ResponseCodeException(response.code, response.msg))
        }
    }

    /**
     * 发送验证码
     */
    fun sendVerificationCode(request: VerificationCodeRequest) =
        fire(Dispatchers.IO) {
            val response = NetWork.sendVerificationCode(request)
            if (response.code == SUCCESS) {
                val isSend = response.data
                Result.success(isSend)
            } else {
                Result.failure(ResponseCodeException(response.code, response.msg))
            }
        }

    /**
     * 发送验证码
     */
    fun getWorkOrderList(request: WorkOrderRequest) =
        fire(Dispatchers.IO) {
            val response = NetWork.getWorkOrderList(request)
            if (response.code == SUCCESS) {
                val isSend = response.data
                Result.success(isSend)
            } else {
                Result.failure(ResponseCodeException(response.code, response.msg))
            }
        }




    private fun <T> fire(context: CoroutineContext, block: suspend () -> Result<T>) =
        liveData<Result<T>>(context) {
            val result = try {
                block()
            } catch (e: Exception) {
                Result.failure<T>(e)
            }
            emit(result)
        }
}