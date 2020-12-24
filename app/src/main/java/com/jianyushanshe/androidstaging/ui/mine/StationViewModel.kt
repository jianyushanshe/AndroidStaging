package com.jianyushanshe.androidstaging.ui.mine

import androidx.lifecycle.*
import com.jianyushanshe.androidstaging.logic.dto.WorkOrderRequest

import com.jianyushanshe.androidstaging.logic.network.MainRepository
import com.jianyushanshe.androidstaging.common.base.BaseViewModel
import com.jianyushanshe.androidstaging.logic.model.WorkOrder

/**
 * Author:wangjianming
 * Time:2020/12/5 17:21
 * Description:StationViewModel
 */
class StationViewModel : BaseViewModel() {
    private var page = 1
    private var size = 5
    var dataList = mutableListOf<WorkOrder.Order>()
    private var workOrderLiveData = MutableLiveData<WorkOrderRequest>()

    val listLiveData = Transformations.switchMap(workOrderLiveData) {
        MainRepository.getWorkOrderList(it)
    }

    override fun getFirstData() {
        page = 1
        workOrderLiveData.value = WorkOrderRequest(page, size)
    }

    override fun getNextData() {
        workOrderLiveData.value = WorkOrderRequest(++page, size)
    }
}