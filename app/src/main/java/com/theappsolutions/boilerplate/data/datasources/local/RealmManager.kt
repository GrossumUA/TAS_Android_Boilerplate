package com.theappsolutions.boilerplate.data.datasources.local

import android.content.Context

import com.theappsolutions.boilerplate.TasBoilerplateSettings
import com.theappsolutions.boilerplate.util.storage.RealmUtils

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.functions.Function
import io.reactivex.schedulers.Schedulers
import io.realm.Realm
import io.realm.RealmConfiguration

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 *
 *
 * Common helper class for Realm DB common actions with data
 */
class RealmManager(private val context: Context) {
    init {
        init()
    }

    fun <R> doWithRealm(callableWithRealm: Function<Realm, R>): Observable<R> {
        return doWithRealmInSpecificThread(callableWithRealm, Schedulers.io())
    }

    fun <R> doWithRealmInSpecificThread(callableWithRealm: Function<Realm, R>, scheduler: Scheduler): Observable<R> {
        return Observable.create<R> { emitter ->
            try {
                val result = callableWithRealm.apply(RealmUtils.getRealm())
                if (result != null) {
                    emitter.onNext(result)
                }
            } catch (ex: Exception) {
                emitter.onError(ex)
            } finally {
                emitter.onComplete()
            }
        }.subscribeOn(scheduler)
    }

    fun <R> doTransaction(callableWithRealm: Function<Realm, R>): Observable<R> {
        return doWithRealm(Function { realm ->
            try {
                realm.beginTransaction()
                return@Function callableWithRealm.apply(realm)
            } catch (e: Exception) {
                e.printStackTrace()
                return@Function null
            } finally {
                realm.commitTransaction()
            }
        })
    }

    private fun init() {
        Realm.init(context)
        val realmConfiguration = RealmConfiguration.Builder()
                .schemaVersion(TasBoilerplateSettings.REALM_LOCAL_DB_VERSION)
                .build()
        Realm.setDefaultConfiguration(realmConfiguration)
    }


}