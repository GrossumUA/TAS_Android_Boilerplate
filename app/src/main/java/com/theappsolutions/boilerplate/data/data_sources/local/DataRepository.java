package com.theappsolutions.boilerplate.data.data_sources.local;


import com.theappsolutions.boilerplate.data.model.realm.CachedProject;

import java.util.List;

import io.reactivex.Observable;
import io.realm.OrderedRealmCollection;


public interface DataRepository {

    Observable<Boolean> addProjects(List<CachedProject> projects);

    Observable<OrderedRealmCollection<CachedProject>> getProjects();

    void clearData();
}
