package com.jianyushanshe.androidstaging.logic.network

import com.jianyushanshe.androidstaging.logic.dto.*
import com.jianyushanshe.androidstaging.logic.model.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

/**
 * Author:wangjianming
 * Time:2020/12/5 10:25
 * Description:MainService
 */
interface MainService {
    @GET
    fun getUser(token: String): Call<User>

    /**
     * 发送验证码接口
     */
    @POST("account/passenger/captcha")
    fun sendVerificationCode(@Body request: VerificationCodeRequest): Call<VerificationCodeResponse>

    /**
     * 获取订单列表
     */
    @GET("customer/order/list")
    fun getWorkOrderList(@QueryMap map: MutableMap<String, Any>): Call<WordOrderResponse>

    /**
     * 登录
     */
    @POST("account/passenger/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    /**
     * 获取版本号
     */
    @GET("account/latest-version")
    fun getVersionCode(@QueryMap map: MutableMap<String, Any>): Call<VersionCodeResponse>
}