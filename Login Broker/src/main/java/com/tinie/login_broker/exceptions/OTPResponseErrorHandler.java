package com.tinie.login_broker.exceptions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinie.login_broker.util.MultiReadHttpResponse;
import com.tinie.login_broker.util.StreamToStringConverter;
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
        //prevents RestTemplate from throwing on getBody() when status code >=400. Alternative is to use statusCode < 400
        httpResponse.getStatusCode();
        var responseRoot = objectMapper.readTree(StreamToStringConverter.toString(httpResponse.getBody()));

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