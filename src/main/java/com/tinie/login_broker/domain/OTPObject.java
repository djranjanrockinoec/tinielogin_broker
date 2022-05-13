package com.tinie.login_broker.domain;

public record OTPObject(long phonenumber, int OTP, long OTPexpiry, String messagestatus) {}
