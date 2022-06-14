package com.tinie.login_broker.exceptions;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public record WhatsappMessagingErrorHandler(long phoneNumber) implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new OTPOrReadFailedException("OTPNOK", phoneNumber);
    }
}
