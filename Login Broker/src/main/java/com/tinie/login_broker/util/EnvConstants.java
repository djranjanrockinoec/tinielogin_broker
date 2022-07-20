package com.tinie.login_broker.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.tinie.login_broker.util.Constants.MILLIS_DAY;

@Component
@ConfigurationProperties("app.constants")
@Data
public class EnvConstants {
    private int sessionExpiryDays;
    private long otpExpirySeconds;
    private String tokenSecret;
    private String otpGenUrl;
    private String userReadUrl;

    public long getSessionExpiryMillis() {
        return MILLIS_DAY * sessionExpiryDays;
    }
}
