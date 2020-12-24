package com.jianyushanshe.androidstaging.logic.dto

import com.jianyushanshe.androidstaging.logic.model.BaseResponse
import com.jianyushanshe.androidstaging.logic.model.User
import com.jianyushanshe.androidstaging.logic.model.WorkOrder

/**
 * Author:wangjianming
 * Time:2020/12/5 16:21
 * Description:MainDto
 */

class LoginRequest(var phoneNum: String, var captcha: String)
class LoginResponse : BaseResponse<User>()
class VerificationCodeRequest(var phoneNum: String)
class VerificationCodeResponse : BaseResponse<Boolean>()
class WorkOrderRequest(var page: Int, var size: Int)
class WordOrderResponse : BaseResponse<WorkOrder>()
