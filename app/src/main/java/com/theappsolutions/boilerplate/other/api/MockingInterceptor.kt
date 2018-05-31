package com.theappsolutions.boilerplate.other.api

import android.os.SystemClock

import java.io.IOException
import java.security.SecureRandom
import java.util.Random

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Special class for mocking REST API via OKHTTP interceptors
 */
class MockingInterceptor private constructor(private val requestsHandler: RequestsHandler) : Interceptor {

    private val random: Random = SecureRandom()

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response? {
        val request = chain.request()
        val path = request.url().encodedPath()
        if (requestsHandler.shouldIntercept(path)) {
            val response = requestsHandler.proceed(request, path)
            val stubDelay = 500 + random.nextInt(2500)
            SystemClock.sleep(stubDelay.toLong())
            return response
        }
        return chain.proceed(request)
    }

    companion object {
        fun create(requestsHandler: RequestsHandler): Interceptor {
            return MockingInterceptor(requestsHandler)
        }
    }
}

