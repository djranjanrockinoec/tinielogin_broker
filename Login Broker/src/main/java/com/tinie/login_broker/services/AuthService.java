package com.tinie.login_broker.services;

import com.tinie.login_broker.config.JWTProcessor;
import com.tinie.login_broker.domain.OTPObject;
import com.tinie.login_broker.domain.UserReadObject;
import com.tinie.login_broker.exceptions.InvalidOTPException;
import com.tinie.login_broker.exceptions.OTPResponseErrorHandler;
import com.tinie.login_broker.exceptions.UserReadResponseErrorHandler;
import com.tinie.login_broker.models.OTPRecords;
import com.tinie.login_broker.repositories.OTPRecordsRepository;
import com.tinie.login_broker.repositories.UserDetailsRepository;
import com.tinie.login_broker.util.EnvConstants;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    private final UserDetailsRepository userDetailsRepository;
    private final RestTemplate OTPRestTemplate;
    private final BeanFactory beanFactory;
    private final JWTProcessor jwtProcessor;
    private final EnvConstants envConstants;
    private final OTPRecordsRepository otpRecordsRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(OTPResponseErrorHandler otpErrorHandler,
                       BeanFactory beanFactory,
                       JWTProcessor jwtProcessor,
                       EnvConstants envConstants,
                       UserDetailsRepository userDetailsRepository,
                       OTPRecordsRepository otpRecordsRepository,
                       BCryptPasswordEncoder passwordEncoder) {
        this.beanFactory = beanFactory;
        this.OTPRestTemplate = beanFactory.getBean(RestTemplate.class, otpErrorHandler);
        this.jwtProcessor = jwtProcessor;
        this.envConstants = envConstants;
        this.userDetailsRepository = userDetailsRepository;
        this.otpRecordsRepository = otpRecordsRepository;
        this.passwordEncoder = passwordEncoder;
    }

    private UserReadResponseErrorHandler userErrHandlerBean(int OTP, long OTPExpiry) {
        return beanFactory.getBean(UserReadResponseErrorHandler.class,
                OTP,
                OTPExpiry);
    }

    public Map<String, Object> genOTPReadUser(long phoneNumber) {

        var result = new HashMap<String, Object>();

        var otpHeaders = new HttpHeaders();
        otpHeaders.setContentType(MediaType.APPLICATION_JSON);
        var request = new HttpEntity<>("""
                {"phonenumber": %d}
                """.formatted(phoneNumber), otpHeaders);
        var otpObject = OTPRestTemplate.postForObject(envConstants.getOtpGenUrl(), request, OTPObject.class);

        var userReadRestTemplate = beanFactory
                .getBean(RestTemplate.class, userErrHandlerBean(otpObject.OTP(), envConstants.getOtpExpirySeconds()));

        var readUserUrl = UriComponentsBuilder.fromHttpUrl(envConstants.getUserReadUrl())
                .queryParam("phonenumber", phoneNumber)
                .encode()
                .toUriString();
        var user = userReadRestTemplate.getForObject(readUserUrl, UserReadObject.class);

        var userDetails = userDetailsRepository.getById(phoneNumber);

        var otpRecordOptional = otpRecordsRepository.findByPhoneNumber(userDetails.getPhoneNumber());
        OTPRecords savedOtpRecord;
        if (otpRecordOptional.isPresent()) {
            savedOtpRecord = otpRecordOptional.get();
            savedOtpRecord.setExpiry(new Date().getTime() + (envConstants.getOtpExpirySeconds() * 1000));
            savedOtpRecord.setHashedOTP(passwordEncoder.encode(Integer.toString(otpObject.OTP())));
            savedOtpRecord = otpRecordsRepository.save(savedOtpRecord);
        } else
            savedOtpRecord = otpRecordsRepository.save(new OTPRecords(userDetails,
                    new Date().getTime() + (envConstants.getOtpExpirySeconds() * 1000),
                    passwordEncoder.encode(Integer.toString(otpObject.OTP()))));

        result.put("phoneNumber", phoneNumber);
        result.put("otp", otpObject.OTP());
        result.put("otpExpiry", envConstants.getOtpExpirySeconds());
        result.put("username", user.username());
        result.put("action", user.action());

        return result;
    }

    public Map<String, Object> verifyOTP(long phoneNumber, int otp) {
        var otpRecordOptional = otpRecordsRepository.findByPhoneNumber(phoneNumber);

        if (otpRecordOptional.isPresent()) {
            var otpRecord = otpRecordOptional.get();
            if (new Date().getTime() < otpRecord.getExpiry() &&
                    passwordEncoder.matches(Integer.toString(otp), otpRecord.getHashedOTP())) {
                var result = new HashMap<String, Object>();
                result.put("sessionExpiry", envConstants.getSessionExpiryDays());
                result.put("token", jwtProcessor.generateToken(otpRecord.getUserDetails()));
                result.put("username", otpRecord.getUserDetails().getUsername());

                //invalidate otp record
                otpRecord.setExpiry(new Date().getTime());
                otpRecordsRepository.save(otpRecord);

                return result;
            }
        }
        throw new InvalidOTPException("Invalid OTP", phoneNumber);
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
