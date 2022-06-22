package com.tinie.login_broker.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

@Component
@Slf4j
public class OTPResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse)
            throws IOException {

        var objectMapper = new ObjectMapper();
        var responseRoot = objectMapper.readTree(httpResponse.getBody());

        log.info("OTP GEN RESPONSE: " + responseRoot);
        var status = (String) responseRoot.get("messagestatus").asText();
        return status.equalsIgnoreCase("NOK");
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse)
            throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonParser = objectMapper.readTree(httpResponse.getBody());

        throw new OTPOrReadFailedException("OTPNOK", jsonParser.get("phonenumber").asLong());
    }
}