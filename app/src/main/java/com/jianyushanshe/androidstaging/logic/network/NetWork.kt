package com.jianyushanshe.androidstaging.logic.network

import com.jianyushanshe.androidstaging.event.DismissLoadingEvent
import com.jianyushanshe.androidstaging.event.ShowLoadingEvent
import com.jianyushanshe.androidstaging.logic.dto.LoginRequest
import com.jianyushanshe.androidstaging.logic.dto.VerificationCodeRequest
import com.jianyushanshe.androidstaging.logic.dto.WorkOrderRequest
import org.greenrobot.eventbus.EventBus
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine


/**
 * Author:wangjianming
 * Time:2020/12/5 10:25
 * Description:NetWork
 */
object NetWork {
    private val mainService = ServiceCreator.create<MainService>()

    //登录
    suspend fun login(request: LoginRequest) = mainService.login(request).await(true)
    //发送验证码
    suspend fun sendVerificationCode(request: VerificationCodeRequest) =
        mainService.sendVerificationCode(request).await(true)
    //获取工单列表
    suspend fun getWorkOrderList(request: WorkOrderRequest) =mainService.getWorkOrderList(
        mutableMapOf("page" to request.page,"size" to request.size)
    ).await(true)
    //获取版本号
    suspend fun getVersionCode() = mainService.getVersionCode( mutableMapOf("target" to 0,"platform" to 1)).await(false)



    private suspend fun <T> Call<T>.await(isShowLoing: Boolean): T {
        if (isShowLoing) EventBus.getDefault().post(ShowLoadingEvent())
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                     EventBus.getDefault().post(DismissLoadingEvent())
                    val body = response.body()
                    if (body != null) continuation.resume(body) else continuation.resumeWithException(
                        RuntimeException("response body is null")
                    )
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                     EventBus.getDefault().post(DismissLoadingEvent())
                    continuation.resumeWithException(t)
                }
            })
        }
    }

}