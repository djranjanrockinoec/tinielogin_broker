package com.tinie.login_broker.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import static com.tinie.login_broker.util.Constants.*;

@Component
@ConfigurationProperties("app.constants")
@Data
public class EnvConstants {
    private int sessionExpiryDays;
    private long otpExpirySeconds;
    private String tokenSecret = "CcdE@t8qS%Q#KJ79cA%$K=$m+!JbpLV#$jp9jvM-XayCuN=cWd+mAUM#xaWgtEsc";
    private String otpGenUrl;
    private String userReadUrl;

    public long getSessionExpiryMillis() {
        return MILLIS_DAY * sessionExpiryDays;
    }
}
