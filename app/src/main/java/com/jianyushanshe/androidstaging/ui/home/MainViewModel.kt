package com.jianyushanshe.androidstaging.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.jianyushanshe.androidstaging.common.base.BaseViewModel
import com.jianyushanshe.androidstaging.logic.network.MainRepository

/**
 * Author:wangjianming
 * Time:2020/12/5 17:21
 * Description:MainViewModel
 */
class MainViewModel : BaseViewModel() {
    private var getVersionCodeLiveData = MutableLiveData<Any?>()
    var versionCodeLiveData = Transformations.switchMap(getVersionCodeLiveData){
        MainRepository.getVersionCode()
    }

    /**
     * 获取版本号
     */
    fun getVersionCode(){
        getVersionCodeLiveData.value = getVersionCodeLiveData.value
    }
}