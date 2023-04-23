package com.example.gamebuddy.util

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val apiType = request
            .tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Api::class.java)?.value
            ?: throw IllegalStateException("ApiType not found. Please add @Api annotation to your method.")

        val baseUrl = EnvironmentManager.getBaseUrl(apiType).toHttpUrl()

        val newUrl = request.url.newBuilder()
            .scheme(baseUrl.scheme)
            .host(baseUrl.host)
            .port(baseUrl.port)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        return chain.proceed(newRequest)
    }
}