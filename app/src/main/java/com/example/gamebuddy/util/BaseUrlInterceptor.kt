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

        val baseUrl = EnvironmentManager.getBaseUrl(apiType).toHttpUrl()

        Timber.d("baseUrl: $baseUrl")

        Timber.d("baseUrl encoded path segments ${baseUrl.encodedPathSegments}, request: encoded path segments ${request.url.encodedPathSegments}")

        val newUrl = HttpUrl.Builder()
            .scheme(baseUrl.scheme)
            .host(baseUrl.host)
            .addPathSegment("${baseUrl.encodedPathSegments[0]}/${request.url.encodedPathSegments.joinToString("/")}")
            .port(baseUrl.port)
            .build()

        val decoded = Uri.decode(newUrl.toString())

        Timber.d("newUrl: $newUrl, newUrlTest: $newUrl, decoded: $decoded")

//        val updatedUrl = if (decoded.contains("%7B") && decoded.contains("%7D")) {
//            decoded.replace("%7B", "").replace("%7D", "")
//        } else {
//            decoded
//        }

        val newRequest = request.newBuilder()
            //.url(newUrl)
            .url(decoded)
            .build()

        return chain.proceed(newRequest)
    }
}