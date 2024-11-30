package com.task.futballconnectapp.data.api.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class ApiKeyInterceptor (private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val requestWithHeaders = originalRequest.newBuilder()
            .addHeader("X-Auth-Token", apiKey)
            .build()
        return chain.proceed(requestWithHeaders)
    }
}