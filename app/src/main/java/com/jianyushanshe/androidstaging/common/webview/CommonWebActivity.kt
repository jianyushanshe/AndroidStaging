package com.jianyushanshe.androidstaging.common.webview

import android.annotation.SuppressLint
import android.content.Context
import android.net.http.SslError
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import android.widget.ProgressBar
import androidx.lifecycle.ViewModelProvider
import com.jianyushanshe.androidstaging.R
import com.jianyushanshe.androidstaging.common.base.BaseActivity
import com.jianyushanshe.androidstaging.common.view.CustomActionbar
import com.jianyushanshe.androidstaging.util.openActivity

/**
 * 公共的webviewActivity
 */
class CommonWebActivity : BaseActivity<WebViewModel>() {
    var url: String = ""
    var title: String = ""
    lateinit var webView: WebView
    lateinit var progressBar: ProgressBar

    override fun initViewModel() {
        viewModel = ViewModelProvider(this).get(WebViewModel::class.java)
    }

    override fun superBaseOnCreate(savedInstanceState: Bundle?) {
        initData()
        initView()
    }

    private fun initView() {
        setContentView(R.layout.activity_common_web)
        setActionbar(CustomActionbar.TYPE.ACTIONBAR_TYPE_OF_BACK_CENTERTEXT, title, null, 0, 0)
        initWebView(url)
    }

    private fun initData() {
        if (intent.hasExtra(EXTRA_URL)) {
            url = intent.getStringExtra(EXTRA_URL) ?: ""
        }
        if (intent.hasExtra(EXTRA_TITLE)) {
            title = intent.getStringExtra(EXTRA_TITLE) ?: ""
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initWebView(url: String) {
        webView = findViewById(R.id.web_view)
        progressBar = findViewById(R.id.progressbar)
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = false
        webSettings.loadWithOverviewMode = true
        webSettings.useWideViewPort = true
        webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        webSettings.domStorageEnabled = true
        webView.webChromeClient = ChromeClient()
        webView.webViewClient = GeneralizeWebClient()
        webView.loadUrl(url)
    }

    inner class ChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView?, newProgress: Int) {
            progressBar.max = 100
            if (newProgress < 100) {
                if (progressBar.visibility == View.GONE)
                    progressBar.visibility = View.VISIBLE
                progressBar.progress = newProgress
            } else {
                progressBar.progress = 100
                progressBar.visibility = View.GONE
            }
            super.onProgressChanged(view, newProgress)
        }

        override fun onReceivedTitle(view: WebView?, title: String?) {
            super.onReceivedTitle(view, title)
        }
    }

    inner class GeneralizeWebClient : WebViewClient() {
        override fun shouldOverrideUrlLoading(
            view: WebView, request: WebResourceRequest
        ): Boolean {
            view.loadUrl(request.url.toString())
            return true
        }

        override fun shouldOverrideKeyEvent(view: WebView?, event: KeyEvent?): Boolean {
            return super.shouldOverrideKeyEvent(view, event)
        }

        override fun onReceivedSslError(
            view: WebView?,
            handler: SslErrorHandler?,
            error: SslError?
        ) {
            handler?.proceed()
        }
    }

    override fun onDestroy() {
        webView.let {
            val parent = webView.parent as ViewGroup
            parent.removeAllViews()
            webView.stopLoading()
            webView.settings.javaScriptEnabled = false
            webView.clearHistory()
            webView.destroy()
        }
        super.onDestroy()
    }


    companion object {
        private const val EXTRA_URL = "EXTRA_URL"
        private const val EXTRA_TITLE = "EXTRA_TITLE"
        fun open(context: Context, url: String, title: String) {
            openActivity<CommonWebActivity>(context) {
                putExtra(EXTRA_URL, url)
                putExtra(EXTRA_TITLE, title)
            }
        }
    }
}