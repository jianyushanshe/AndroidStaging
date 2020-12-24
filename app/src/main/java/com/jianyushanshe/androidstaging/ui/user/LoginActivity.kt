package com.jianyushanshe.androidstaging.ui.user

import android.content.Context
import android.content.Intent
import android.os.Bundle

import androidx.lifecycle.ViewModelProvider
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.extension.showToast
import com.jianyushanshe.androidstaging.logic.network.exceptionDispose
import com.jianyushanshe.androidstaging.common.base.BaseActivity
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.ui.home.MainActivity
import com.jianyushanshe.androidstaging.util.getStringResId
import com.jianyushanshe.androidstaging.util.openActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 登录页
 */
class LoginActivity : BaseActivity<LoginViewModel>() {

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)
    }

    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_login)
        setActionbar(CustomActionbar.TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT, "登录", null, 0, 0)
        observer()

        bt_login.setOnClickListener {
            viewModel.login(et_account.text.toString(), et_password.text.toString())
        }
        btGetCode.setOnClickListener {
            viewModel.sendVerificationCode(et_account.text.toString())
        }
    }



    /**
     * 观察数据变化
     */
    private fun observer() {
        viewModel.verificationCodeLiveData.observe(this) {
            if (it.exceptionOrNull() != null) {
                //有异常，处理异常
                exceptionDispose(it.exceptionOrNull())
                return@observe
            }
            //处理正常流程
            val response = it.getOrNull()
            getStringResId(R.string.get_code_success).showToast()
        }

        viewModel.userLiveData.observe(this) {
            if (it.exceptionOrNull() != null) {
                //有异常，处理异常
                exceptionDispose(it.exceptionOrNull())
                return@observe
            }
            //处理正常流程
            val response = it.getOrNull()
            response?.let { it1 -> viewModel.cacheUser(it1) }
            "登录成功".showToast()
            openActivity<MainActivity>(this)
            finish()
        }
    }

    companion object {
        fun start(context: Context) {
            context.startActivity(Intent(context, LoginActivity::class.java))
        }
    }
}