package com.theappsolutions.boilerplate;

import android.support.annotation.NonNull;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;
import com.theappsolutions.boilerplate.ui.projects.ProjectListPresenter;
import com.theappsolutions.boilerplate.ui.projects.ProjectListView;
import com.theappsolutions.boilerplate.ui.splash.SplashPresenter;
import com.theappsolutions.boilerplate.ui.splash.SplashView;
import com.theappsolutions.boilerplate.util.RxSchedulersOverrideRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import io.reactivex.Observable;
import io.realm.OrderedRealmCollection;
import io.realm.RealmList;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the implementation of {@link com.theappsolutions.boilerplate.ui.projects.ProjectListPresenter}
 */
@RunWith(MockitoJUnitRunner.class)
public class ProjectsPresenterTest extends CommonPresenterTest<ProjectListView, ProjectListPresenter> {

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new
            RxSchedulersOverrideRule();

    @Mock
    ProjectListView mockView;
    @Mock
    DataManager dataManager;
    @Mock
    AnalyticsManager analyticsManagerMock;

    @InjectMocks
    private ProjectListPresenter presenter;

    @Before
    public void setUp() {
        super.setUp();
        Observable<OrderedRealmCollection<CachedProject>> mockData = Observable.just(new RealmList<>());

        when(dataManager.getProjects()).thenReturn(mockData);
        when(dataManager.syncProjects()).thenReturn(Observable.just(true));
    }

    @NonNull
    @Override
    protected ProjectListPresenter getPresenter() {
        return presenter;
    }

    @NonNull
    @Override
    protected ProjectListView getView() {
        return mockView;
    }

    @Test
    public void initTest() {
        presenter.init();

        verify(mockView, once()).setupList(Mockito.any());
        verifySync();
    }

    @Test
    public void syncTest() {
        presenter.onRefresh();

        verifySync();
    }

    private void verifySync() {
        verify(mockView, once()).stopRefreshAnimation();
        verify(analyticsManagerMock, once()).logEvent(AnalyticsEvent.SYNC_PROJECTS);
    }
}
