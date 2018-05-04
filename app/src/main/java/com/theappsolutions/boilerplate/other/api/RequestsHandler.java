package com.theappsolutions.boilerplate.other.api;

import android.content.Context;
import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.TasBoilerplateApplication;
import com.theappsolutions.boilerplate.data.datasources.remote.RetrofitBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import okhttp3.Response;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 * Manages mocking of OKHTTP responses with {@link MockingInterceptor}
 */
public class RequestsHandler {

    private final Map<String, String> mResponsesMap = new HashMap<>();
    private final Context context;

    public RequestsHandler(Context context) {
        this.context = context;
        mResponsesMap.put("/login", "mock_responses/login.json");
        mResponsesMap.put("/user/repos", "mock_responses/repositories.json");
        mResponsesMap.put("/projects", "mock_responses/projects.json");
    }

    public boolean shouldIntercept(@NonNull String path) {
        Set<String> keys = mResponsesMap.keySet();
        for (String interceptUrl : keys) {
            if (path.contains(interceptUrl)) {
                return true;
            }
        }
        return false;
    }

    @NonNull
    public Response proceed(@NonNull Request request, @NonNull String path) {
        Set<String> keys = mResponsesMap.keySet();
        String token = TasBoilerplateApplication.get(context).getComponent().preferencesHelper().getToken().orElse(null);
        if (token != null && (request.header(RetrofitBuilder.AUTHORIZATION_KEY) == null ||
                !request.header(RetrofitBuilder.AUTHORIZATION_KEY).contains(token))) {
            return OkHttpResponse.error(request, 401, "Not authorized");
        }

        for (String interceptUrl : keys) {
            if (path.contains(interceptUrl)) {
                String mockResponsePath = mResponsesMap.get(interceptUrl);
                return createResponseFromAssets(request, mockResponsePath);
            }
        }

        return OkHttpResponse.error(request, 500, "Incorrectly intercepted request");
    }

    @NonNull
    private Response createResponseFromAssets(@NonNull Request request, @NonNull String assetPath) {
        try {
            final InputStream stream = context.getAssets().open(assetPath);
            //noinspection TryFinallyCanBeTryWithResources
            try {
                return OkHttpResponse.success(request, stream);
            } finally {
                stream.close();
            }
        } catch (IOException e) {
            return OkHttpResponse.error(request, 500, e.getMessage());
        }
    }
}
