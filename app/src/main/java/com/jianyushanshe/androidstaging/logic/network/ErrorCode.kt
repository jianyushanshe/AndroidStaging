package com.jianyushanshe.androidstaging.logic.network

import com.google.gson.JsonSyntaxException
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.event.DismissLoadingEvent
import com.jianyushanshe.androidstaging.event.NetExceptionEvent
import com.jianyushanshe.androidstaging.event.ReLoginEvent
import com.jianyushanshe.androidstaging.extension.showToast
import com.jianyushanshe.androidstaging.common.exception.ResponseCodeException
import com.jianyushanshe.androidstaging.extension.logE
import com.jianyushanshe.androidstaging.util.getStringResId
import org.greenrobot.eventbus.EventBus
import java.net.ConnectException
import java.net.NoRouteToHostException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Author:wangjianming
 * Time:2020/12/8 9:54
 * Description:ErrorCode  错误码和异常处理
 */

const val DEFAULT = -23333; // 默认的错误码
const val NO_CONNECTIVITY = -666; // 没有网络连接
const val SUCCESS = 200;
const val PARAM_NOT_FOUND = 400; // 参数不存在
const val PARAM_ERROR = 401; // 参数错误
const val REQUEST_BODY_INVALID = 402; // 请求体无效
const val URL_NOT_FOUND = 404; // 请求地址不存在
const val CONTENT_TYPE_INVALID = 405; // 负载类型有误
const val AUTH_EXPIRED = 406; // 登录状态过期
const val SYSTEM_BUSY = 500;
const val TEL_NUMBER_NOT_EXIST = 500000//电话不存在

/**
 * 异常统一处理
 */
fun exceptionDispose(e: Throwable?) {
    logE("TAG_EXCEPTION",e?.message,e!!)
    EventBus.getDefault().post(DismissLoadingEvent())
    when (e) {
        is ConnectException ->
            EventBus.getDefault()
                .post(NetExceptionEvent(getStringResId(R.string.network_connect_error)))
        is SocketTimeoutException ->
            EventBus.getDefault()
                .post(NetExceptionEvent(getStringResId(R.string.network_connect_timeout)))
        is NoRouteToHostException ->
            EventBus.getDefault()
                .post(NetExceptionEvent(getStringResId(R.string.no_route_to_host)))
        is UnknownHostException ->
            EventBus.getDefault()
                .post(NetExceptionEvent(getStringResId(R.string.network_error)))
        is JsonSyntaxException -> getStringResId(R.string.json_data_error).showToast()
        //自定义错误码处理
        is ResponseCodeException -> when (e.responseCode) {
            TEL_NUMBER_NOT_EXIST -> {//电话号不存在
                e.msg?.showToast()
            }
            AUTH_EXPIRED -> {//重新登录
                EventBus.getDefault().post(ReLoginEvent(getStringResId(R.string.dialog_login_view_tip)))
            }
            else -> getStringResId(R.string.network_response_code_error) + e.responseCode
        }
        else -> {
            EventBus.getDefault()
                .post(NetExceptionEvent(getStringResId(R.string.unknown_error)))
        }
    }
}