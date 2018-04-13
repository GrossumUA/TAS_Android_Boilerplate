package com.theappsolutions.boilerplate.data.data_sources.remote;

import com.theappsolutions.boilerplate.data.model.api.LoginResponse;
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.adapter.rxjava2.Result;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestService {

    @FormUrlEncoded
    @POST("/login")
    Observable<Result<LoginResponse>> login(
            @Field("username") String username,
            @Field("password") String password);

    @POST("/projects")
    Observable<Result<List<ProjectsResponse>>> getProjects();
}
