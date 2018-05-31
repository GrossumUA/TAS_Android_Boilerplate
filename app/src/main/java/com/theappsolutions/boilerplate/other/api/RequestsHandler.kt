package com.theappsolutions.boilerplate.other.api

import android.content.Context

import com.theappsolutions.boilerplate.TasBoilerplateApplication
import com.theappsolutions.boilerplate.data.datasources.remote.RetrofitBuilder

import java.io.IOException
import java.io.InputStream
import java.util.HashMap

import okhttp3.Request
import okhttp3.Response

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manages mocking of OKHTTP responses with [MockingInterceptor]
 */
class RequestsHandler(private val context: Context) {

    private val mResponsesMap: HashMap<String, String> = HashMap<String, String>()
    init {
        mResponsesMap["/login"] = "mock_responses/login.json"
        mResponsesMap["/user/repos"] = "mock_responses/repositories.json"
        mResponsesMap["/projects"] = "mock_responses/projects.json"
    }

    fun shouldIntercept(path: String): Boolean {
        val keys = mResponsesMap.keys
        for (interceptUrl in keys) {
            if (path.contains(interceptUrl)) {
                return true
            }
        }
        return false
    }

    fun proceed(request: Request, path: String): Response? {
        val keys = mResponsesMap.keys
        val token = TasBoilerplateApplication.get(context).getComponent().preferencesHelper().getToken().orElse(null)
        if (token != null && (request.header(RetrofitBuilder.AUTHORIZATION_KEY) == null || !request.header(RetrofitBuilder.AUTHORIZATION_KEY).contains(token))) {
            return OkHttpResponse.error(request, 401, "Not authorized")
        }

        for (interceptUrl in keys) {
            if (path.contains(interceptUrl)) {
                val mockResponsePath = mResponsesMap.get(interceptUrl)
                return mockResponsePath?.let { createResponseFromAssets(request, it) }
            }
        }

        return OkHttpResponse.error(request, 500, "Incorrectly intercepted request")
    }

    private fun createResponseFromAssets(request: Request, assetPath: String): Response? {
        try {
            val stream = context.assets.open(assetPath)

            try {
                return OkHttpResponse.success(request, stream)
            } finally {
                stream.close()
            }
        } catch (e: IOException) {
            return e.message?.let { OkHttpResponse.error(request, 500, it) }
        }

    }
}
