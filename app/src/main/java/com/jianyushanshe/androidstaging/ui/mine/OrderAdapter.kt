package com.jianyushanshe.androidstaging.ui.mine

import android.content.Context
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.CommonBaseAdapter
import com.jianyushanshe.androidstaging.common.holder.ViewHolder
import com.jianyushanshe.androidstaging.logic.model.WorkOrder

/**
 * Author:wangjianming
 * Time:2020/12/21 14:48
 * Description:OrderAdapter
 */
class OrderAdapter(mContext: Context) : CommonBaseAdapter<WorkOrder.Order>(mContext) {

    override val layoutId = R.layout.work_order_history_list_item

    override fun conner(holder: ViewHolder, entity: WorkOrder.Order) {
        holder.setText(R.id.tvTitle, entity.carNumber)
    }
}