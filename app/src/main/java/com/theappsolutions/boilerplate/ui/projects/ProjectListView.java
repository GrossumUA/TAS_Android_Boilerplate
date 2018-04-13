package com.theappsolutions.boilerplate.ui.projects;


import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.ui.base.BaseView;

import io.realm.OrderedRealmCollection;

public interface ProjectListView extends BaseView {

    void setupList(OrderedRealmCollection<CachedProject> data);

    void stopRefreshAnimation();
}