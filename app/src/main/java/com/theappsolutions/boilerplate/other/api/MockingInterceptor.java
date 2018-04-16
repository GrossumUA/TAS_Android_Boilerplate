package com.theappsolutions.boilerplate.other.api;

import android.os.SystemClock;
import android.support.annotation.NonNull;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Random;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Special class for mocking REST API via OKHTTP interceptors
 */
public class MockingInterceptor implements Interceptor {

    private final RequestsHandler requestsHandler;
    private final Random random;

    private MockingInterceptor(RequestsHandler requestsHandler) {
        this.requestsHandler = requestsHandler;
        random = new SecureRandom();
    }

    @NonNull
    public static Interceptor create(RequestsHandler requestsHandler) {
        return new MockingInterceptor(requestsHandler);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        String path = request.url().encodedPath();
        if (requestsHandler.shouldIntercept(path)) {
            Response response = requestsHandler.proceed(request, path);
            int stubDelay = 500 + random.nextInt(2500);
            SystemClock.sleep(stubDelay);
            return response;
        }
        return chain.proceed(request);
    }
}

