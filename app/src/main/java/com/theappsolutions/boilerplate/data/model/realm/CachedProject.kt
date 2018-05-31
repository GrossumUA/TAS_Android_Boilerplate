package com.theappsolutions.boilerplate.data.model.realm

import com.theappsolutions.boilerplate.data.model.api.ProjectsResponse

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
open class CachedProject(
        @PrimaryKey
        var id: Long = 0,
        var name: String = "",
        var type: String = "",
        var logoSrc: String = "") : RealmObject() {
    companion object {
        fun fromProjectResponse(projectsResponse: ProjectsResponse): CachedProject {
            return CachedProject(projectsResponse.id, projectsResponse.name,
                    projectsResponse.type, projectsResponse.logoSrc)
        }
    }
}
