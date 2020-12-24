package com.jianyushanshe.androidstaging.logic.network

import android.content.Context
import android.os.Build
import android.webkit.WebSettings

import com.google.gson.GsonBuilder
import com.jianyushanshe.androidstaging.BuildConfig.*
import com.jianyushanshe.androidstaging.MyApp
import com.jianyushanshe.androidstaging.extension.screenPixel
import com.jianyushanshe.androidstaging.util.*
import com.jianyushanshe.androidstaging.util.UserManager.getToken
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

/**
 * Author:wangjianming
 * Time:2020/12/5 10:24
 * Description:ServiceCreator
 */
object ServiceCreator {
    private val BASE_URL = when (URL_TYPE) {
        GET_DEVELOP -> DEVELOP_URL
        GET_BETA -> BETA_URL
        GET_RELEASE -> RELEASE_URL
        else -> "null"
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(getLogInterceptor())
        .addInterceptor(HeaderInterceptor())
        .addInterceptor(getUserAgentInrercept())
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(httpClient)
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(
            GsonConverterFactory.create(
                GsonBuilder().registerTypeAdapterFactory(
                    GsonTypeAdapterFactory()
                ).create()
            )
        )
        .build()


    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)

    fun getOkHttpClient(): OkHttpClient = httpClient


    class HeaderInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()
            val request = original.newBuilder().apply {
                addHeader("token", getToken().toString())
            }.build()
            return chain.proceed(request)
        }
    }

    private fun getLogInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        if (DEBUG) {
            interceptor.level = HttpLoggingInterceptor.Level.BODY
        } else {
            interceptor.level = HttpLoggingInterceptor.Level.BASIC
        }
        return interceptor
    }

    /**
     * 添加user-Agent
     *
     * @return
     */
    private fun getUserAgentInrercept(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request()
                .newBuilder()
                .removeHeader("User-Agent") //移除旧的
                .addHeader("User-Agent", getUserAgent(MyApp.context)) //添加真正的头部
                .build()
            chain.proceed(request)
        }
    }

    /**
     * 获取user-agent
     *
     * @param context
     * @return
     */
    private fun getUserAgent(context: Context): String {
        var userAgent: String? = ""
        userAgent =
            try {
                WebSettings.getDefaultUserAgent(context)
            } catch (e: Exception) {
                System.getProperty("http.agent")
            }
        val sb = StringBuffer()
        var i = 0
        val length = userAgent!!.length
        while (i < length) {
            val c = userAgent[i]
            if (c <= '\u001f' || c >= '\u007f') {
                sb.append(String.format("\\u%04x", c.toInt()))
            } else {
                sb.append(c)
            }
            i++
        }
        return sb.toString()
    }


    class BasicParamsInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val originalRequest = chain.request()
            val originalHttpUrl = originalRequest.url()
            val url = originalHttpUrl.newBuilder().apply {
                addQueryParameter("udid", getDeviceSerial())
                addQueryParameter("vc", appVersionCode.toString())
                addQueryParameter("vn", appVersionName)
                addQueryParameter("size", screenPixel())
                addQueryParameter("deviceModel", deviceModel)
                addQueryParameter("first_channel", deviceBrand)
                addQueryParameter("last_channel", deviceBrand)
                addQueryParameter("system_version_code", "${Build.VERSION.SDK_INT}")
            }.build()
            val request = originalRequest.newBuilder().url(url)
                .method(originalRequest.method(), originalRequest.body()).build()
            return chain.proceed(request)
        }
    }
}