package com.tinie.login_broker.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class UserReadResponseErrorHandler implements ResponseErrorHandler {

    private final int OTP;
    private final long OTPExpiry;

    public UserReadResponseErrorHandler(int OTP, long OTPExpiry) {
        this.OTP = OTP;
        this.OTPExpiry = OTPExpiry;
    }

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        var objectMapper = new ObjectMapper();
        var responseRoot = objectMapper.readTree(response.getBody());

        var status = (String) responseRoot.get("readstatus").asText();
        return status.equalsIgnoreCase("NOK");
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonParser = objectMapper.readTree(response.getBody());

        if (jsonParser.get("action").asText().equalsIgnoreCase("Register"))
            throw new UserReadRegisterException(
                    jsonParser.get("action").asText(),
                    jsonParser.get("phonenumber").asLong(),
                    OTP,
                    OTPExpiry);
        else
            throw new OTPOrReadFailedException("READNOK", jsonParser.get("phonenumber").asLong());
    }
}
