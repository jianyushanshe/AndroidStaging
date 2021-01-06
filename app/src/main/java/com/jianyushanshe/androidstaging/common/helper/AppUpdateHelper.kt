package com.jianyushanshe.androidstaging.common.helper

import android.content.Context
import android.widget.TextView
import com.allenliu.versionchecklib.callback.CommitClickListener
import com.allenliu.versionchecklib.callback.OnCancelListener
import com.allenliu.versionchecklib.v2.AllenVersionChecker
import com.allenliu.versionchecklib.v2.builder.DownloadBuilder
import com.allenliu.versionchecklib.v2.builder.NotificationBuilder
import com.allenliu.versionchecklib.v2.builder.UIData
import com.allenliu.versionchecklib.v2.callback.CustomVersionDialogListener
import com.allenliu.versionchecklib.v2.callback.ForceUpdateListener
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.view.BaseDialog
import com.jianyushanshe.androidstaging.util.ActivityCollector
import com.jianyushanshe.androidstaging.util.getStringResId

/**
 * Author:wangjianming
 * Time:2021/1/6 10:09
 * Description:AppUpdateHelper 升级App辅助类
 * 调用AppUpdateHelper.updataApp()函数进行弹窗显示和升级操作
 */
object AppUpdateHelper {
    private var builder: DownloadBuilder? = null
    private var versionName: String? = null
    /**
     * 升级App
     *
     * @param context     上下文
     * @param downloadUrl 下载链接
     * @param info        更新内容
     * @param isForce     是否强制更新
     */
    fun updataApp(
        context: Context,
        downloadUrl: String,
        versionCode: Int,
        versionName: String,
        info: String,
        isForce: Boolean
    ) {
        this.versionName = versionName
        builder = AllenVersionChecker
            .getInstance()
            .downloadOnly(crateUIData(downloadUrl, versionName, info))
        if (isForce) {
            builder?.customVersionDialogListener = createCustomDialogForce()
            builder?.forceUpdateListener = ForceUpdateListener { //强制更新，用户点击返回按钮，关闭所有页面
                ActivityCollector.removeAll()
            }
        } else {
            builder?.customVersionDialogListener = createCustomDialogTwo()
        }
        //自定义下载路径
        builder?.downloadAPKPath = context.getExternalFilesDir(null).toString() + "/Haylion/SmartMetroApp/"
        builder?.isShowNotification = true //是否显示通知栏
        builder?.isSilentDownload = false //静默下载 默认false.检测本地安装包和当前版本都小于线上版本时，在后台下载apk。下载完成后在显示升级弹窗
        builder?.isForceRedownload = false //如果本地有安装包缓存也会重新下载apk
        //缓存策略：如果本地有安装包，首先判断与当前运行的程序的versionCode是否不一致，然后判断是否有传入最新的 versionCode，如果传入的versionCode大于本地的，重新从服务器下载，否则使用缓存
        builder?.newestVersionCode = versionCode //传入线上最新版本号，根据最新版本号查询本地缓存的版本号。
        builder?.apkName = "HaylionSmartMetro" //设置apk名称
        builder?.isRunOnForegroundService = true //前台服务
        builder?.isShowDownloadingDialog = false //是否显示下载对话框默认true
        builder?.notificationBuilder = createCustomNotification() //通知样式
        builder?.onCancelListener = OnCancelListener { //取消按钮
            if (isForce) {
                return@OnCancelListener
            }
        }
        builder?.readyDownloadCommitClickListener = CommitClickListener {
            //确认下载
            if (isForce) {
                builder?.isShowDownloadingDialog = true //是否显示下载对话框默认true
            }
        }
        builder?.executeMission(context)
    }

    /**
     * 务必用库传回来的context 实例化你的dialog
     * 自定义的dialog UI参数展示，使用versionBundle
     *
     * @return 强制升级dialog
     */
    private fun createCustomDialogForce(): CustomVersionDialogListener {
        return CustomVersionDialogListener { context, versionBundle ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.dialog_update_one)
            val textView: TextView = baseDialog.findViewById(R.id.tv_msg)
            textView.text = versionBundle.content
            val tvTitle: TextView = baseDialog.findViewById(R.id.tv_title)
            tvTitle.text = versionBundle.title
            val tvVersionName: TextView = baseDialog.findViewById(R.id.tv_version_name)
            tvVersionName.text = versionName
            baseDialog
        }
    }

    /**
     * 普通升级dialog
     *
     * @return
     */
    private fun createCustomDialogTwo(): CustomVersionDialogListener {
        return CustomVersionDialogListener { context, versionBundle ->
            val baseDialog = BaseDialog(context, R.style.BaseDialog, R.layout.dialog_update_two)
            val textView: TextView = baseDialog.findViewById(R.id.tv_msg)
            textView.text = versionBundle.content
            val tvTitle: TextView = baseDialog.findViewById(R.id.tv_title)
            tvTitle.text = versionBundle.title
            val tvVersionName: TextView = baseDialog.findViewById(R.id.tv_version_name)
            tvVersionName.text = versionName
            baseDialog
        }
    }

    /**
     * @return
     * @important 使用请求版本功能，可以在这里设置downloadUrl
     * 这里可以构造UI需要显示的数据
     * UIData是一个Bundle，用于存放用于UI展示的一些数据，后面自定义界面时候可以拿来用
     */
    private fun crateUIData(downloadUrl: String, versionName: String?, info: String): UIData? {
        val uiData = UIData.create()
        uiData.title = "发现新版本"
        uiData.downloadUrl = downloadUrl
        uiData.content = info
        return uiData
    }

    private fun createCustomNotification(): NotificationBuilder? {
        return NotificationBuilder.create()
            .setRingtone(true)
            .setIcon(R.mipmap.app_icon)
            .setTicker("custom_ticker")
            .setContentTitle(getStringResId(R.string.app_name))
            .setContentText("升级中")
    }
}