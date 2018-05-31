package com.theappsolutions.boilerplate.data

import com.annimon.stream.Stream
import com.annimon.stream.function.Function
import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.data.datasources.local.DataRepository
import com.theappsolutions.boilerplate.data.model.api.LoginResponse
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse
import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.data.datasources.remote.RestApiClient
import com.theappsolutions.boilerplate.util.data.RandomUtils

import io.reactivex.Observable
import io.reactivex.ObservableSource
import io.realm.OrderedRealmCollection

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class DataManager(private val restApiClient: RestApiClient,
                  private val preferencesManager: PreferencesManager,
                  private val dataRepository: DataRepository) {

    fun getProjects(): Observable<OrderedRealmCollection<CachedProject>> = dataRepository.getProjects()

    fun tryLogin(username: String, password: String): Observable<LoginResponse> {
        return restApiClient.login(username, password)
                .doOnNext { (token) ->
                    preferencesManager.setToken(token)
                }
    }

    fun syncProjects(): Observable<Boolean> {
        return restApiClient.getProjects()
                .map { projectsResponses ->
                    Stream.of(RandomUtils.generateRandList(projectsResponses,
                            TasBoilerplateSettings.TestSettings.RESPONSE_ELEMENT_COUNT))
                            .map { projectsResponse -> CachedProject.fromProjectResponse(projectsResponse) }
                            .toList()
                }
                .flatMap { projects -> dataRepository.addProjects(projects) }
    }

    fun clearData() {
        dataRepository.clearData()
    }
}
