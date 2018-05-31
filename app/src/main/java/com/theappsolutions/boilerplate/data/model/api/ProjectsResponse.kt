package com.theappsolutions.boilerplate.data.model.api

import com.google.gson.annotations.SerializedName

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
data class ProjectsResponse(
        @SerializedName("id") val id: Long,
        @SerializedName("name") val name: String,
        @SerializedName("type") val type: String,
        @SerializedName("logoSrc") val logoSrc: String
)