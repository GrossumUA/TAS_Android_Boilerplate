package com.theappsolutions.boilerplate.data.data_sources.local;

import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.util.storage_utils.RealmUtils;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.realm.OrderedRealmCollection;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class DataRepositoryImpl implements DataRepository {

    private RealmManager realmManager;

    public DataRepositoryImpl(RealmManager realmManager) {
        this.realmManager = realmManager;
    }


    @Override
    public Observable<Boolean> addProjects(List<CachedProject> projects) {
        return realmManager.doTransaction(realm -> {
            realm.copyToRealmOrUpdate(projects);
            return true;
        });
    }

    @Override
    public Observable<OrderedRealmCollection<CachedProject>> getProjects() {
        return realmManager.doWithRealmInSpecificThread(realm -> realm.where(CachedProject.class).findAllSorted("id"),
                AndroidSchedulers.mainThread());
    }

    @Override
    public void clearData() {
        RealmUtils.clearRealmTable(CachedProject.class);
    }


}
