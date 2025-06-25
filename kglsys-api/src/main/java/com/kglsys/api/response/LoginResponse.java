package com.kglsys.api.response;

import lombok.Data;

/**
 * DTO for the API response upon successful authentication.
 */
@Data
public class LoginResponse {
    private String accessToken;
    private String tokenType = "Bearer";

    public LoginResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
