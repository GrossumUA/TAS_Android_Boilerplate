package com.theappsolutions.boilerplate.data.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * @author Severyn Parkhomenko s.parkhomenko@theappsolutions.com
 * @copyright (c) 2018 TheAppSolutions. (https://theappsolutions.com)
 */
public class LoginResponse {

    @SerializedName("token")
    private String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String toString() {
        return
                "LoginResponse{" +
                        "token = '" + token + '\'' +
                        "}";
    }
}