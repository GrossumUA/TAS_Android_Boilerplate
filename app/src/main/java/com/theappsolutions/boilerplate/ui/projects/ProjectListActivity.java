package com.theappsolutions.boilerplate.ui.projects;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.theappsolutions.boilerplate.R;
import com.theappsolutions.boilerplate.customviews.CustomToolbar;
import com.theappsolutions.boilerplate.customviews.RecyclerViewWithEmptyMode;
import com.theappsolutions.boilerplate.data.model.realm.CachedProject;
import com.theappsolutions.boilerplate.ui.base.BaseActivity;
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity;
import com.theappsolutions.boilerplate.ui.base.BasePresenter;

import javax.inject.Inject;

import butterknife.BindView;
import io.realm.OrderedRealmCollection;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class ProjectListActivity extends BaseMvpActivity implements ProjectListView {

    @BindView(R.id.rv_list)
    RecyclerViewWithEmptyMode rvList;

    @BindView(R.id.tv_empty_view)
    View tvEmptyView;

    @BindView(R.id.srl_refresh)
    SwipeRefreshLayout swlRefresh;

    @Inject
    ProjectListPresenter projectListPresenter;

    @Override
    protected int getContentViewRes() {
        return R.layout.activity_projects;
    }

    @Override
    protected CustomToolbar.Config getToolbarConfig() {
        return CustomToolbar.Config.defaultConfig().hasBackBtn(false).hasSettingsBtn(true);
    }

    public static Intent getIntent(BaseActivity activity) {
        return new Intent(activity, ProjectListActivity.class);
    }

    public static void startWithAnimation(BaseActivity activity) {
        activity.startActivity(getIntent(activity));
        activity.animateActivityChangingToRight();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setUpView();
        projectListPresenter.attachView(this);
        projectListPresenter.init();
    }

    @Override
    public void stopRefreshAnimation() {
        swlRefresh.setRefreshing(false);
    }

    /*
     * It is good practice to null the reference from the view to the adapter when it is no longer needed.
     * Because the <code>RealmRecyclerViewAdapter</code> registers itself as a <code>RealmResult.ChangeListener</code>
     * the view may still be reachable if anybody is still holding a reference to the <code>RealmResult>.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        rvList.setAdapter(null);
    }

    @Override
    public BasePresenter getBasePresenter() {
        return projectListPresenter;
    }

    private void setUpView() {
        rvList.setHasFixedSize(true);
        rvList.setOnUpdateListener(() -> swlRefresh.setRefreshing(false));
        swlRefresh.setOnRefreshListener(() -> projectListPresenter.onRefresh());
        rvList.setEmptyView(tvEmptyView);
    }

    @Override
    public void setupList(OrderedRealmCollection<CachedProject> data) {
        rvList.setAdapter(new ProjectsAdapter(data));
    }
}
