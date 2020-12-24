package com.jianyushanshe.androidstaging.ui.user

import androidx.lifecycle.*
import com.jianyushanshe.androidstaging.logic.dto.LoginRequest
import com.jianyushanshe.androidstaging.logic.dto.VerificationCodeRequest
import com.jianyushanshe.androidstaging.logic.model.User
import com.jianyushanshe.androidstaging.logic.network.MainRepository
import com.jianyushanshe.androidstaging.common.base.BaseViewModel

/**
 * Author:wangjianming
 * Time:2020/12/5 17:21
 * Description:LoginViewModel
 */
class LoginViewModel : BaseViewModel() {


    /**
     * 缓存用户数据到本地
     */
    fun cacheUser(userEntity: User) = MainRepository.cacheUser(userEntity)

    /**
     * 本地获取用户数据
     */
    fun getUser() = MainRepository.getUser()

    /**
     * 可观察的数据请求liveData在switchMap中进行观察
     */
    private var loginLiveData = MutableLiveData<LoginRequest>()
    private var sendVerificationCodeLiveData = MutableLiveData<VerificationCodeRequest>()

    /**
     * 将结果转换为可观察的userLiveData,在activity中进行观察
     */
    val userLiveData = Transformations.switchMap(loginLiveData) {
        MainRepository.login(it)
    }


    /**
     * 发送验证码
     */
    val verificationCodeLiveData = Transformations.switchMap(sendVerificationCodeLiveData) {
        MainRepository.sendVerificationCode(it)
    }

    /**
     * 登录
     */
    fun login(account: String, captcha: String) {
        loginLiveData.value = LoginRequest(account, captcha)
    }

    /**
     * 发送验证码
     */
    fun sendVerificationCode(telNumber: String) {
        sendVerificationCodeLiveData.value = VerificationCodeRequest(telNumber)
    }



}