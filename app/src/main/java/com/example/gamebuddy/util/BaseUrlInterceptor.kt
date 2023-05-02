package com.example.gamebuddy.util

import android.net.Uri
import okhttp3.HttpUrl
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


        Timber.d("ALOOOOOO apiType: $apiType")

        val baseUrl = EnvironmentManager.getBaseUrl(apiType).toHttpUrl()

        Timber.d("ALOOOOOO baseUrl: $baseUrl")

//        val newUrl = baseUrl.newBuilder()
//            .scheme(baseUrl.scheme)
//            .host(baseUrl.host)
//            .addPathSegment(request.url.encodedPathSegments[0])
//            .port(baseUrl.port)
//            .build()

        Timber.d("baseUrl: ${baseUrl.encodedPathSegments}, request: ${request.url.encodedPathSegments}")

        val newUrl = HttpUrl.Builder()
            .scheme(baseUrl.scheme)
            .host(baseUrl.host)
            .addPathSegment("${baseUrl.encodedPathSegments[0]}/${request.url.encodedPathSegments.joinToString("/")}")
            .port(baseUrl.port)
            .build()

        val decoded = Uri.decode(newUrl.toString())

        Timber.d("newUrl: $newUrl, newUrlTest: $newUrl, decoded: $decoded")

        val newRequest = request.newBuilder()
            //.url(newUrl)
            .url(decoded)
            .build()

        return chain.proceed(newRequest)
    }
}