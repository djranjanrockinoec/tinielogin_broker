package com.tinie.login_broker.exceptions;

import lombok.Getter;

@Getter
public class UserReadRegisterException extends RuntimeException{
    private final int OTP;
    private final long phoneNumber;
    private final long OTPExpiry;

    public UserReadRegisterException(String message,
                                     long phoneNumber,
                                     int OTP,
                                     long OTPExpiry){
        super(message);
        this.phoneNumber = phoneNumber;
        this.OTP = OTP;
        this.OTPExpiry = OTPExpiry;
    }
}
