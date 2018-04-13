package com.theappsolutions.boilerplate;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.PreferencesManager;
import com.theappsolutions.boilerplate.data.data_sources.local.DataRepository;
import com.theappsolutions.boilerplate.data.data_sources.remote.RestApiClient;
import com.theappsolutions.boilerplate.data.model.api.LoginResponse;
import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse;
import com.theappsolutions.boilerplate.mock.LoginResponseMock;
import com.theappsolutions.boilerplate.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.observers.TestObserver;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the
 * Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g.
 * RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is
 * calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new
            RxSchedulersOverrideRule();

    @Mock
    RestApiClient restApiClientMock;
    @Mock
    DataRepository dataRepositoryMock;
    @Mock
    PreferencesManager preferencesManagerMock;

    @InjectMocks
    private DataManager dataManager;

    @Before
    public void setUp() {
    }

    @Test
    public void tryLoginTest() {
        String loginTestData = "test@test.com";
        String passwordTestData = "1Qqqqq";
        Observable<LoginResponse> loginResponseMock = Observable.just(LoginResponseMock.generate());

        when(restApiClientMock.login(loginTestData, passwordTestData)).thenReturn(loginResponseMock);

        TestObserver<LoginResponse> testSubscriber = dataManager.tryLogin(loginTestData, passwordTestData).test();
        testSubscriber.awaitTerminalEvent();
        verify(preferencesManagerMock, times(1)).setToken(Mockito.any());
    }

    @Test
    public void trySyncProjects() {
        Observable<List<ProjectsResponse>> syncProjectsMock = Observable.just(new ArrayList<ProjectsResponse>());

        when(restApiClientMock.getProjects()).thenReturn(syncProjectsMock);

        TestObserver<Boolean> testSubscriber = dataManager.syncProjects().test();
        testSubscriber.awaitTerminalEvent();

        verify(dataRepositoryMock, times(1)).addProjects(Mockito.anyList());
    }
}
