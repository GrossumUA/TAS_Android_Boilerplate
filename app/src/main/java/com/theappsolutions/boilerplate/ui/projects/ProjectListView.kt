package com.theappsolutions.boilerplate.ui.projects

import com.theappsolutions.boilerplate.data.model.realm.CachedProject
import com.theappsolutions.boilerplate.ui.base.BaseView

import io.realm.OrderedRealmCollection

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
interface ProjectListView : BaseView {

    fun setupList(data: OrderedRealmCollection<CachedProject>)

    fun stopRefreshAnimation()
}