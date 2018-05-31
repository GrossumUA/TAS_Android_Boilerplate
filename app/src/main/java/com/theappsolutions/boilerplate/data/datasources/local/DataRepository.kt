package com.theappsolutions.boilerplate.data.datasources.local

import com.theappsolutions.boilerplate.data.model.realm.CachedProject

import io.reactivex.Observable
import io.realm.OrderedRealmCollection

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface DataRepository {

    fun getProjects(): Observable<OrderedRealmCollection<CachedProject>>

    fun addProjects(projects: List<CachedProject>): Observable<Boolean>

    fun clearData()
}
