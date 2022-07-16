package com.tinie.login_broker.dtos.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class OTPGenResponse {
    @JsonProperty("phonenumber")
    private long phonenumber;
    @JsonProperty("OTP")
    private int OTP;
    @JsonProperty("OTPexpiry")
    private long OTPExpiry;
    private String username;
    private String action;
}
