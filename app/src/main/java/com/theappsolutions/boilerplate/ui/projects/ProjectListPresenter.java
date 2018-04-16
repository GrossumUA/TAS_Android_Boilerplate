package com.theappsolutions.boilerplate.ui.projects;

import com.theappsolutions.boilerplate.data.DataManager;
import com.theappsolutions.boilerplate.injection.ConfigPersistent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent;
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
public class ProjectListPresenter extends BasePresenter<ProjectListView> {

    private DataManager dataManager;
    private AnalyticsManager analyticsManager;

    @Inject
    public ProjectListPresenter(DataManager dataManager, AnalyticsManager analyticsManager) {
        this.dataManager = dataManager;
        this.analyticsManager = analyticsManager;
    }

    public void init() {
        Disposable disposable = dataManager.getProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> getMvpView().ifPresent(projectListView -> projectListView.setupList(list)), getErrorConsumer());
        addToDisposables(disposable);
        syncProjects();
    }

    public void onRefresh() {
        syncProjects();
    }

    private void syncProjects() {
        Disposable disposable = dataManager.syncProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(list -> {
                    analyticsManager.logEvent(AnalyticsEvent.SYNC_PROJECTS);
                    getMvpView().ifPresent(ProjectListView::stopRefreshAnimation);
                }, getErrorConsumer());
        addToDisposables(disposable);
    }
}