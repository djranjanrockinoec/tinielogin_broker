package com.tinie.login_broker.dtos.requests;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@Data
public class VerifyTokenRequest {
    @ApiModelProperty(required = true, value = "The User's phone number")
    @Min(1)
    private long phonenumber;

    @ApiModelProperty(required = true, value = "Token to be verified")
    @NotBlank(message = "Token value must not be empty")
    private String token;
}
