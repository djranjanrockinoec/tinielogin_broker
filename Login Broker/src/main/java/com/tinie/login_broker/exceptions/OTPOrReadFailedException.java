package com.tinie.login_broker.exceptions;

import lombok.Getter;

public class OTPOrReadFailedException extends RuntimeException{
    @Getter
    private final long phoneNumber;
    public OTPOrReadFailedException(String message, long phoneNumber){
        super(message);
        this.phoneNumber = phoneNumber;
    }
}
