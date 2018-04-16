package com.theappsolutions.boilerplate.data;

import com.annimon.stream.Stream;
import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.data.data_sources.local.DataRepository;
import com.theappsolutions.boilerplate.data.model.api.LoginResponse;
import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.data.data_sources.remote.RestApiClient;
import com.theappsolutions.boilerplate.util.data_utils.RandomUtils;

import io.reactivex.Observable;
import io.realm.OrderedRealmCollection;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class DataManager {

    private final RestApiClient restApiClient;
    private final DataRepository dataRepository;
    private final PreferencesManager preferencesManager;

    public DataManager(RestApiClient restApiClient,
                       PreferencesManager preferencesManager,
                       DataRepository dataRepository) {
        this.restApiClient = restApiClient;
        this.preferencesManager = preferencesManager;
        this.dataRepository = dataRepository;
    }

    public Observable<LoginResponse> tryLogin(String username, String password) {
        return restApiClient.login(username, password)
                .doOnNext(loginResponse -> {
                    String token = loginResponse.getToken();
                    preferencesManager.setToken(token);
                });
    }

    public Observable<Boolean> syncProjects() {
        return restApiClient.getProjects()
                .map(projectsResponses -> Stream.of(RandomUtils.generateRandList(projectsResponses,
                        TasBoilerplateSettings.TestSettings.RESPONSE_ELEMENT_COUNT))
                        .map(CachedProject::fromProjectResponse).toList())
                .flatMap(dataRepository::addProjects);
    }

    public Observable<OrderedRealmCollection<CachedProject>> getProjects() {
        return dataRepository.getProjects();
    }

    public void clearData() {
        dataRepository.clearData();
    }
}
