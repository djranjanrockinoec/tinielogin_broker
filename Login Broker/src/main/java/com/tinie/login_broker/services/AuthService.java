package com.tinie.login_broker.services;

import com.tinie.login_broker.config.JWTProcessor;
import com.tinie.login_broker.domain.OTPObject;
import com.tinie.login_broker.domain.UserReadObject;
import com.tinie.login_broker.exceptions.OTPResponseErrorHandler;
import com.tinie.login_broker.exceptions.UserReadResponseErrorHandler;
import com.tinie.login_broker.util.EnvConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private RestTemplate OTPRestTemplate;
    private final BeanFactory beanFactory;
    private final JWTProcessor jwtProcessor;
    private EnvConstants envConstants;

    @Autowired
    public AuthService(OTPResponseErrorHandler otpErrorHandler,
                       BeanFactory beanFactory,
                       JWTProcessor jwtProcessor,
                       EnvConstants envConstants) {
        this.beanFactory = beanFactory;
        this.OTPRestTemplate = beanFactory.getBean(RestTemplate.class, otpErrorHandler);
        this.jwtProcessor = jwtProcessor;
        this.envConstants = envConstants;
    }

    private UserReadResponseErrorHandler userErrHandlerBean(int OTP, long OTPExpiry) {
        return beanFactory.getBean(UserReadResponseErrorHandler.class,
                OTP,
                OTPExpiry,
                envConstants.getSessionExpiryDays());
    }

    public Map<String, Object> genOTPReadUser(long phoneNumber) {

        var result = new HashMap<String, Object>();

        var otpObject = OTPRestTemplate.getForObject(envConstants.getOtpGenUrl(), OTPObject.class);

        var userReadRestTemplate = beanFactory
                .getBean(RestTemplate.class, userErrHandlerBean(otpObject.OTP(), envConstants.getOtpExpirySeconds()));
        var user = userReadRestTemplate.getForObject(envConstants.getUserReadUrl(), UserReadObject.class);

        result.put("phoneNumber", phoneNumber);
        result.put("otp", otpObject.OTP());
        result.put("otpExpiry", otpObject.OTPexpiry());
        result.put("username", user.username());
        result.put("sessionExpiry", envConstants.getSessionExpiryDays());
        result.put("action", user.action());
        result.put("token", jwtProcessor.generateToken(String.valueOf(phoneNumber)));

        return result;
    }

    public boolean verifyJWT(String subject, String jwt) {

        try {
            var decodedJWT = jwtProcessor.verifyAndDecodeToken(jwt);
            return decodedJWT.getSubject().equalsIgnoreCase(subject);
        } catch (Exception e) {
            return false;
        }
    }
}
