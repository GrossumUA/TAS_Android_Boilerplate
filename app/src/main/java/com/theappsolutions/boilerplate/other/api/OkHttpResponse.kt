package com.theappsolutions.boilerplate.other.api

import java.io.IOException
import java.io.InputStream

import okhttp3.MediaType
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import okio.Buffer

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Provides manual building of OKHTTP responses
 */
object OkHttpResponse {

    private val EMPTY_BODY = ByteArray(0)
    private val APPLICATION_JSON = MediaType.parse("application/json")

    @Throws(IOException::class)
    fun success(request: Request, stream: InputStream): Response {
        val buffer = Buffer().readFrom(stream)
        return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("OK")
                .body(ResponseBody.create(APPLICATION_JSON, buffer.size(), buffer))
                .build()
    }

    fun error(request: Request, code: Int, message: String): Response {
        return Response.Builder()
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .code(code)
                .message(message)
                .body(ResponseBody.create(APPLICATION_JSON, ByteArray(0)))
                .build()
    }
}
