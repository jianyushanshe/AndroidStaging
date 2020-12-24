package com.jianyushanshe.androidstaging.logic.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

/**
 * Author:wangjianming
 * Time:2020/12/7 14:38
 * Description:BaseResponse
 */
open class BaseResponse<T> :Serializable{

    @SerializedName("message")
    var msg: String? = null
    var code: Int = -1
    var data: T? = null
}