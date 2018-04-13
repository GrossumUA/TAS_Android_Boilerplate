package com.theappsolutions.boilerplate.mock;

import com.theappsolutions.boilerplate.data.model.api.LoginResponse;

public class LoginResponseMock extends LoginResponse {

    private static String mockToken = "mock_token";

    public LoginResponseMock(String token) {
        super(token);
    }

    public static LoginResponse generate() {
        return new LoginResponse(mockToken);
    }
}
