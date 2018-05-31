package com.theappsolutions.boilerplate.data.datasources.remote

import android.content.Context

import com.google.gson.Gson
import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.injection.ApplicationContext
import com.theappsolutions.boilerplate.other.api.MockingInterceptor
import com.theappsolutions.boilerplate.other.api.RequestsHandler
import com.theappsolutions.boilerplate.util.other.NetworkUtils

import java.io.IOException

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

import com.theappsolutions.boilerplate.util.other.NetworkUtils.isNetworkConnected

/**
 * @author Dmytro Yakovlev d.yakovlev@theappsolutions.com
 * @copyright (c) 2017 TheAppSolutions. (https://theappsolutions.com)
 */
object RetrofitBuilder {

    val AUTHORIZATION_KEY = "Authorization"

    fun buildRetrofitInstance(@ApplicationContext context: Context,
                              requestsHandler: RequestsHandler, gson: Gson, mapHeaders: Map<String, String>,
                              url: String, preferencesManager: PreferencesManager): Retrofit {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val builder = OkHttpClient.Builder()
                .addInterceptor(ConnectionCheckInterceptor(context))
                .addInterceptor(MaiHeaderInterceptor(preferencesManager))
                .addInterceptor(MockingInterceptor.create(requestsHandler))
                .addInterceptor(logging)
                .addNetworkInterceptor {
                    val request = it.request().newBuilder()
                    mapHeaders.forEach { (key, value) ->
                        request.addHeader(key, value)
                    }
                    it.proceed(request.build())
                }

        val httpClient = builder.build()
        return Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(httpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    private class MaiHeaderInterceptor(val preferencesManager: PreferencesManager) : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            return chain.proceed(
                    preferencesManager.getToken().map { token ->
                        request.newBuilder()
                                .header(AUTHORIZATION_KEY, token)
                                .method(request.method(), request.body())
                                .build()
                    }.orElse(request)
            )
        }
    }

    private class ConnectionCheckInterceptor(val appContext: Context) : Interceptor {

        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            if (!isNetworkConnected(appContext)) {
                throw NetworkUtils.noConnectionException(appContext)
            }
            return chain.proceed(chain.request())
        }
    }
}
