package com.theappsolutions.boilerplate.ui.projects

import com.theappsolutions.boilerplate.data.DataManager
import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.injection.ConfigPersistent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsEvent
import com.theappsolutions.boilerplate.other.analytics.AnalyticsManager
import com.theappsolutions.boilerplate.ui.base.BasePresenter

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import io.realm.OrderedRealmCollection

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
@ConfigPersistent
class ProjectListPresenter @Inject
constructor(private val dataManager: DataManager, private val analyticsManager: AnalyticsManager) : BasePresenter<ProjectListView>() {

    fun init() {
        val disposable = dataManager.getProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { list -> getMvpView().ifPresent { projectListView -> projectListView.setupList(list) } }
        addToDisposables(disposable)
        syncProjects()
    }

    fun onRefresh() {
        syncProjects()
    }

    private fun syncProjects() {
        val disposable: Disposable
        disposable = dataManager.syncProjects()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    analyticsManager.logEvent(AnalyticsEvent.SYNC_PROJECTS)
                    this@ProjectListPresenter.getMvpView().ifPresent { projectListView -> projectListView.stopRefreshAnimation() }
                }
        addToDisposables(disposable)
    }
}