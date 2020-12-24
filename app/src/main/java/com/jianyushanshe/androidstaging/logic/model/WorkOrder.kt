package com.jianyushanshe.androidstaging.logic.model


data class WorkOrder(val finished: Finished) : Model() {
    data class Finished(val list: ArrayList<Order>?)
    data class Order(val carNumber: String?)
}