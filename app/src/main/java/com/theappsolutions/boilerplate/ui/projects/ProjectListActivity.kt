package com.theappsolutions.boilerplate.ui.projects

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.view.View

import com.theappsolutions.boilerplate.R
import com.theappsolutions.boilerplate.customviews.CustomToolbar
import com.theappsolutions.boilerplate.customviews.RecyclerViewWithEmptyMode
import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.ui.base.BaseActivity
import com.theappsolutions.boilerplate.ui.base.BaseMvpActivity
import com.theappsolutions.boilerplate.ui.base.BasePresenter

import javax.inject.Inject

import butterknife.BindView
import com.theappsolutions.boilerplate.TasBoilerplateApplication
import io.realm.OrderedRealmCollection
import kotlinx.android.synthetic.main.activity_projects.*
import kotlinx.android.synthetic.main.common_empty_view.*

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
class ProjectListActivity : BaseMvpActivity(), ProjectListView {

    @Inject
    lateinit var projectListPresenter: ProjectListPresenter

    override fun getContentViewRes(): Int {
        return R.layout.activity_projects
    }

    override fun getToolbarConfig(): CustomToolbar.Config? {
        return CustomToolbar.Config.defaultConfig().hasBackBtn(false).hasSettingsBtn(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityComponent()?.inject(this);
        setUpView()
        projectListPresenter.attachView(this)
        projectListPresenter.init()
    }

    override fun stopRefreshAnimation() {
        srl_refresh?.isRefreshing = false
    }

    /*
     * It is good practice to null the reference from the view to the adapter when it is no longer needed.
     * Because the <code>RealmRecyclerViewAdapter</code> registers itself as a <code>RealmResult.ChangeListener</code>
     * the view may still be reachable if anybody is still holding a reference to the <code>RealmResult>.
     */
    override fun onDestroy() {
        super.onDestroy()
        rv_list?.adapter = null
    }

    override fun getBasePresenter(): BasePresenter<*> {
        return projectListPresenter
    }

    private fun setUpView() {
        rv_list?.setHasFixedSize(true)
        //rv_list?.setOnUpdateListener({ srl_refresh?.isRefreshing = false })
        srl_refresh?.setOnRefreshListener { projectListPresenter.onRefresh() }
        rv_list?.setEmptyView(tv_empty_view)
    }

    override fun setupList(data: OrderedRealmCollection<CachedProject>) {
        rv_list?.adapter = ProjectsAdapter(data)
    }

    companion object {

        fun getIntent(activity: BaseActivity): Intent {
            return Intent(activity, ProjectListActivity::class.java)
        }

        fun startWithAnimation(activity: BaseActivity) {
            activity.startActivity(getIntent(activity))
            activity.animateActivityChangingToRight()
        }
    }
}
