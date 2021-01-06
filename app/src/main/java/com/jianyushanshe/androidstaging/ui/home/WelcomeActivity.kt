package com.jianyushanshe.androidstaging.ui.home

import android.Manifest
import android.os.Bundle
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation
import androidx.lifecycle.ViewModelProvider
import com.gyf.immersionbar.ImmersionBar
import com.jianyushanshe.androidstaging.BuildConfig
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.BaseActivity
import com.jianyushanshe.androidstaging.common.helper.AppUpdateHelper
import com.jianyushanshe.androidstaging.extension.edit
import com.jianyushanshe.androidstaging.logic.network.exceptionDispose
import com.jianyushanshe.androidstaging.ui.user.LoginActivity
import com.jianyushanshe.androidstaging.util.UserManager
import com.jianyushanshe.androidstaging.util.getStringResId
import com.jianyushanshe.androidstaging.util.openActivity
import com.jianyushanshe.androidstaging.util.sharedPreferences
import com.permissionx.guolindev.PermissionX
import kotlinx.android.synthetic.main.activity_welcome.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 启动页
 */
class WelcomeActivity : BaseActivity<WelcomeViewModel>() {
    private val job by lazy { Job() }
    private val splashDuration = 3 * 1000L
    private val alphaAnimation by lazy {
        AlphaAnimation(0.5f, 1.0f).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    private val scaleAnimation by lazy {
        ScaleAnimation(
            1f,
            1.05f,
            1f,
            1.05f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        ).apply {
            duration = splashDuration
            fillAfter = true
        }
    }

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WelcomeViewModel::class.java)
    }

    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        requestWriteExternalStoragePermission()
    }

    override fun setStatusBarBackgroud(statusBarColor: Int) {
        //沉浸式
        ImmersionBar.with(this).init()
    }

    private fun requestWriteExternalStoragePermission() {
        PermissionX.init(this@WelcomeActivity)
            .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .onExplainRequestReason { scope, deniedList ->
                val message = getStringResId(R.string.request_permission_picture_processing)
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    getStringResId(R.string.ok),
                    getStringResId(R.string.cancel)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                val message = getStringResId(R.string.request_permission_picture_processing)
                scope.showForwardToSettingsDialog(
                    deniedList,
                    message,
                    getStringResId(R.string.settings),
                    getStringResId(R.string.cancel)
                )
            }
            .request { _, _, _ ->
                requestReadPhoneStatePermission()
            }
    }

    private fun requestReadPhoneStatePermission() {
        PermissionX.init(this@WelcomeActivity).permissions(Manifest.permission.READ_PHONE_STATE)
            .onExplainRequestReason { scope, deniedList ->
                val message = getStringResId(R.string.request_permission_access_phone_info)
                scope.showRequestReasonDialog(
                    deniedList,
                    message,
                    getStringResId(R.string.ok),
                    getStringResId(R.string.cancel)
                )
            }
            .onForwardToSettings { scope, deniedList ->
                val message = getStringResId(R.string.request_permission_access_phone_info)
                scope.showForwardToSettingsDialog(
                    deniedList,
                    message,
                    getStringResId(R.string.settings),
                    getStringResId(R.string.cancel)
                )
            }
            .request { _, _, _ ->
                setContentView(R.layout.activity_welcome)
                ivSlogan.startAnimation(alphaAnimation)
                ivSplashPicture.startAnimation(scaleAnimation)
                CoroutineScope(job).launch {
                    delay(splashDuration)
                    isLoginAndStart()
                    finish()
                }
                isFirstEntryApp = false
            }
    }

    private fun isLoginAndStart() {
        if (UserManager.isLogin()) {
            openActivity<MainActivity>(this)
        } else {
            openActivity<LoginActivity>(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    companion object {
        //是否首次进入app
        var isFirstEntryApp: Boolean
            get() = sharedPreferences.getBoolean("is_first_entry_app", true)
            set(value) = sharedPreferences.edit { putBoolean("is_first_entry_app", value) }
    }
}