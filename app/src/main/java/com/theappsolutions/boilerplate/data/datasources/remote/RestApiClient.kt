package com.theappsolutions.boilerplate.data.datasources.remote

import com.theappsolutions.boilerplate.data.PreferencesManager
import com.theappsolutions.boilerplate.data.model.api.LoginResponse
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse
import com.theappsolutions.boilerplate.util.other.ApiUtils

import javax.inject.Inject
import javax.inject.Singleton

import io.reactivex.Observable
import io.reactivex.ObservableTransformer
import javax.xml.transform.Result

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Singleton
class RestApiClient @Inject
constructor(private val restService: RestService,
            private val apiUtils: ApiUtils,
            private val prefHelper: PreferencesManager) {

    fun getProjects(): Observable<List<ProjectsResponse>> = restService.getProjects().compose({ apiUtils.decorate(it) })

    fun login(username: String, password: String): Observable<LoginResponse> {
        return restService.login(username, password).compose({ apiUtils.decorate(it) })
    }
}
