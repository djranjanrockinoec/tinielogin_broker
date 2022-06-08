package com.tinie.login_broker.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class TokenResponse {
    private String token;
    @JsonProperty("sessionexpiry")
    private int sessionExpiry;
    @JsonProperty("phonenumber")
    private long phonenumber;
    private String username;
}
