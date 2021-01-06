package com.jianyushanshe.androidstaging.logic.model

/**
 * Author:wangjianming
 * Time:2021/1/6 10:54
 * Description:AppVersionEntity
 *     versionNumber;//版本号
versionName;//版本名称
minVersionNumber;//最低版本 小于最低版本强制更新
uploadUrl;//下载url
updatedInstructions;//版本说明
forcedUpdatedInstructions;//强制更新说明
 */
data class AppVersionEntity(
    val versionNumber: Int,
    val versionName: String,
    val minVersionNumber: Int,
    val uploadUrl: String,
    val updatedInstructions: String?,
    val forcedUpdatedInstructions: String?
) : Model()
