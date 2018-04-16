package com.theappsolutions.boilerplate.util.other_utils;

import com.google.gson.Gson;
import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.data.model.common.ApiErrorResult;
import com.theappsolutions.boilerplate.other.exceptions.ApiException;
import com.theappsolutions.boilerplate.other.exceptions.UserReadableException;
import com.theappsolutions.boilerplate.other.functions.Function2;
import com.theappsolutions.boilerplate.other.functions.Function3;

import java.io.IOException;
import java.util.Locale;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;
import retrofit2.adapter.rxjava2.Result;

/**
 * @author Severyn Parkhomenko <sparkhomenko@grossum.com>
 * @copyright (c) Grossum. (http://www.grossum.com)
 *
 * Set of methods that wrap main transformations and checking while working with REST API
 */
public class ApiUtils {

    private Gson gson;

    public ApiUtils(Gson gson) {
        this.gson = gson;
    }

    public <ApiDataType> Observable<ApiDataType> doWithApiVersionAndLocale(Function2<String, String,
            Observable<ApiDataType>> obsProvider) {
        return obsProvider.call(TasBoilerplateSettings.API_VERSION, Locale.getDefault().getCountry());
    }

    public <ApiDataType, Param1Type> Observable<ApiDataType> doWithApiVersionAndLocale(
            Function3<String, String, Param1Type, Observable<ApiDataType>> obsProvider,
            Param1Type param) {
        return obsProvider.call(TasBoilerplateSettings.API_VERSION, Locale.getDefault().getCountry(), param);
    }

    /**
     * Decorates retrofit API observable.
     * Throws 'onError'-handled exceptions in case http result contains errors
     */
    public <ApiDataType> Observable<ApiDataType> decorate(
            Observable<Result<ApiDataType>> apiObservable) {
        return apiObservable
                .subscribeOn(Schedulers.io())
                .map(this::getOrThrowError);
    }

    private <T> T getOrThrowError(Result<T> apiResult) {
        if (apiResult.isError()) {
            @SuppressWarnings("ThrowableResultOfMethodCallIgnored")
            Throwable error = apiResult.error();
            if (error instanceof UserReadableException) {
                throw ((UserReadableException) error);
            } else {
                throw new RuntimeException(apiResult.error());
            }
        } else {
            Response<T> response = apiResult.response();
            if (response == null) {
                throw new UserReadableException("Unexpected successful response with empty response");
            } else if (response.isSuccessful()) {
                T body = response.body();
                if (body == null) {
                    throw new ApiException(response.code(), "Unexpected successful response with empty body");
                }
                return body;
            } else {
                try {
                    String errorBody = response.errorBody().string();
                    if (errorBody == null || errorBody.isEmpty()) {
                        throw new ApiException(response.code(), response.message() == null ?
                                "Unexpected error response with empty error body" : response.message());
                    }
                    ApiErrorResult errorResult = gson.fromJson(errorBody, ApiErrorResult.class);
                    throw new ApiException(response.code(), errorResult.getMessage());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
