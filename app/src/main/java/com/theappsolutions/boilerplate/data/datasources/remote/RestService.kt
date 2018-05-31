package com.theappsolutions.boilerplate.data.datasources.remote

import com.theappsolutions.boilerplate.data.model.api.LoginResponse
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse

import io.reactivex.Observable
import retrofit2.adapter.rxjava2.Result
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface RestService {

    @POST("/projects")
    fun getProjects(): Observable<Result<List<ProjectsResponse>>>

    @FormUrlEncoded
    @POST("/login")
    fun login(@Field("username") username: String, @Field("password") password: String): Observable<Result<LoginResponse>>
}
