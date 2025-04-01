package com.hivislav.vknewsclient.data.network.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object VkApiFactory {
    private const val BASE_URL = "https://api.vk.com/method/"

    // TODO добавить когда прокину DI
//    private const val ACCESS_TOKEN_QUERY_NAME = "access_token"
//
//    class TokenInterceptor() : Interceptor {
//        override fun intercept(chain: Interceptor.Chain): Response {
//            val oldRequest = chain.request()
//            val url = oldRequest.url.newBuilder()
//                .apply {
//                    setQueryParameter(ACCESS_TOKEN_QUERY_NAME, oldRequest.url.queryParameter(ACCESS_TOKEN_QUERY_NAME))
//
//                    if (!oldRequest.url.queryParameterNames.contains(ACCESS_TOKEN_QUERY_NAME) && token.isNotEmpty()) {
//                        addQueryParameter(ACCESS_TOKEN_QUERY_NAME, token)
//                    }
//                }.build()
//
//            val newRequest = oldRequest.newBuilder().url(url).build()
//            return chain.proceed(newRequest)
//        }
//    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(
            HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
        )
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val api: VkApi = retrofit.create()
}
