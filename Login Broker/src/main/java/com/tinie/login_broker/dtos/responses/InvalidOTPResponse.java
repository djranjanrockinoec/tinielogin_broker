package com.tinie.login_broker.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class InvalidOTPResponse {
    @JsonProperty("phonenumber")
    private long phonenumber;
    private String message;
}