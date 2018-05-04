package com.theappsolutions.boilerplate.data.datasources.remote;

import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.data.model.api.LoginResponse;
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse;
import com.theappsolutions.boilerplate.util.other.ApiUtils;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.Observable;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@Singleton
public class RestApiClient {

    private final ApiUtils apiUtils;
    private RestService restService;
    private PreferencesManager prefHelper;

    @Inject
    public RestApiClient(RestService restService,
                         ApiUtils apiUtils,
                         PreferencesManager prefHelper) {
        this.apiUtils = apiUtils;
        this.restService = restService;
        this.prefHelper = prefHelper;
    }

    public Observable<LoginResponse> login(String username, String password) {
        return restService.login(username, password).compose(apiUtils::decorate);
    }

    public Observable<List<ProjectsResponse>> getProjects() {
        return restService.getProjects().compose(apiUtils::decorate);
    }
}
