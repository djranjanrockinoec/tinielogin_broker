package com.tinie.login_broker.exceptions;

import lombok.Getter;

@Getter
public class InvalidOTPException extends RuntimeException{
    private final long phoneNumber;

    public InvalidOTPException(String message, long phoneNumber){
        super(message);
        this.phoneNumber = phoneNumber;
    }
}
