package com.theappsolutions.boilerplate.data.model.api

import com.google.gson.annotations.SerializedName

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
data class LoginResponse(
        @SerializedName("token") val token: String) {
    override fun toString(): String {
        return "LoginResponse{ token = '$token' }"
    }
}
