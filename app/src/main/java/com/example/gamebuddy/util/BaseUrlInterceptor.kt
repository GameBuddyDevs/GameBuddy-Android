package com.example.gamebuddy.util

import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Invocation
import timber.log.Timber
import javax.inject.Inject

class BaseUrlInterceptor @Inject constructor(): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val apiType = request
            .tag(Invocation::class.java)
            ?.method()
            ?.getAnnotation(Api::class.java)?.value
            ?: throw IllegalStateException("ApiType not found. Please add @Api annotation to your method.")

        Timber.d("apiType: ${apiType.url}")

        Timber.d("EnvironmentManager.getBaseUrl(apiType): ${EnvironmentManager.getBaseUrl(apiType)}")

        val baseUrl = EnvironmentManager.getBaseUrl(apiType).toHttpUrl()

        Timber.d("baseUrl: $baseUrl")

        val newUrl = request.url.newBuilder()
            .scheme(baseUrl.scheme)
            .host(baseUrl.host)
            .port(baseUrl.port)
            .build()

        val newRequest = request.newBuilder()
            .url(newUrl)
            .build()

        Timber.d("request.url: ${request.url}, request.url.host: ${request.url.host}, request.url.port: ${request.url.port}, request.url.scheme: ${request.url.scheme}, request.url.encodedPath: ${request.url.encodedPath}")
        Timber.d("newRequest.url: ${newRequest.url}, newRequest.url.host: ${newRequest.url.host}, newRequest.url.port: ${newRequest.url.port}, newRequest.url.scheme: ${newRequest.url.scheme}, newRequest.url.encodedPath: ${newRequest.url.encodedPath}")

        return chain.proceed(newRequest)
    }
}