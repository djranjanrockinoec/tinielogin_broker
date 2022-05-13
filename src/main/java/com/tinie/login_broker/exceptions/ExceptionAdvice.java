package com.tinie.login_broker.exceptions;

import com.tinie.login_broker.dtos.responses.ActionResponse;
import com.tinie.login_broker.dtos.responses.OTPGenResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * Handle thrown {@link UserReadRegisterException} and return a {@link ResponseEntity} with status code of {@literal 404}
     * @param e Instance of {@link UserReadRegisterException} thrown
     * @return Instance of {@link ResponseEntity} containing {@link OTPGenResponse}
     */
    @ExceptionHandler(value = UserReadRegisterException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ResponseEntity<OTPGenResponse> registerUserHandler(UserReadRegisterException e){
        OTPGenResponse response = new OTPGenResponse();
        response.setAction(e.getMessage());
        response.setOTP(e.getOTP());
        response.setOTPExpiry(e.getOTPExpiry());
        response.setSessionExpiry(e.getSessionExpiry());
        response.setPhonenumber(e.getPhoneNumber());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handle thrown {@link OTPOrReadFailedException} and return a {@link ResponseEntity} with status code of {@literal 500}
     * @param e Instance of {@link OTPOrReadFailedException} thrown
     * @return Instance of {@link ResponseEntity} containing {@link ActionResponse}
     */
    @ExceptionHandler(value = OTPOrReadFailedException.class)
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ActionResponse> OTPSendFailed(OTPOrReadFailedException e){
        ActionResponse response = new ActionResponse();
        response.setPhonenumber(e.getPhoneNumber());
        response.setAction(e.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
