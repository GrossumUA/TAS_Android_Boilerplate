package com.theappsolutions.boilerplate.data.datasources.local

import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.util.storage.RealmUtils

import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Function
import io.realm.OrderedRealmCollection
import io.realm.Realm

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class DataRepositoryImpl(private val realmManager: RealmManager) : DataRepository {

    override fun getProjects(): Observable<OrderedRealmCollection<CachedProject>> {
        return realmManager.doWithRealmInSpecificThread(Function { realm ->
            realm.where(CachedProject::class.java)
                .findAllSorted("id") },
                AndroidSchedulers.mainThread())
    }

    override fun clearData() {
        RealmUtils.clearRealmTable(CachedProject::class.java)
    }


    override fun addProjects(projects: List<CachedProject>): Observable<Boolean> {
        return realmManager.doTransaction(Function { realm ->
            realm.copyToRealmOrUpdate(projects)
            true
        })
    }
}
