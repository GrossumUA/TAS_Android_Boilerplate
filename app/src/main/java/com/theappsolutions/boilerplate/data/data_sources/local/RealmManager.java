package com.theappsolutions.boilerplate.data.data_sources.local;

import android.content.Context;

import com.theappsolutions.boilerplate.TasBoilerplateSettings;
import com.theappsolutions.boilerplate.util.storage_utils.RealmUtils;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Common helper class for Realm DB common actions with data
 */
public class RealmManager {

    private Context context;

    public RealmManager(Context context) {
        this.context = context;
        init();
    }

    public <R> Observable<R> doWithRealm(Function<Realm, R> callableWithRealm) {
        return doWithRealmInSpecificThread(callableWithRealm, Schedulers.io());
    }

    public <R> Observable<R> doWithRealmInSpecificThread(Function<Realm, R> callableWithRealm, Scheduler scheduler) {
        return Observable.<R>create(emitter -> {
            try {
                R result = callableWithRealm.apply(RealmUtils.getRealm());
                if (result != null) {
                    emitter.onNext(result);
                }
            } catch (Exception ex) {
                emitter.onError(ex);
            } finally {
                emitter.onComplete();
            }
        }).subscribeOn(scheduler);
    }

    public <R> Observable<R> doTransaction(Function<Realm, R> callableWithRealm) {
        return doWithRealm(realm -> {
            try {
                realm.beginTransaction();
                return callableWithRealm.apply(realm);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            } finally {
                realm.commitTransaction();
            }
        });
    }

    private void init() {
        Realm.init(context);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(TasBoilerplateSettings.REALM_LOCAL_DB_VERSION)
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}