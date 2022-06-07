package com.tinie.login_broker.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.tinie.login_broker.models.LoginRecords;
import com.tinie.login_broker.models.UserDetails;
import com.tinie.login_broker.repositories.LoginEntryRepository;
import com.tinie.login_broker.util.EnvConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTProcessor {

    @Autowired
    EnvConstants envConstants;
    @Autowired
    LoginEntryRepository loginEntryRepository;

    public String generateToken(UserDetails subject){
        Date now = new Date();
        var epochSeconds = (now.getTime()/1000);//reduce precision to seconds to accommodate JWT implementation
        var token =  JWT.create()
                .withSubject(String.valueOf(subject.getPhoneNumber()))
                .withIssuedAt(now)
                .withExpiresAt(new Date(now.getTime() + (envConstants.getSessionExpiryMillis())))
                .sign(Algorithm.HMAC512(envConstants.getTokenSecret()));
        var loginEntryOptional = loginEntryRepository.findByPhoneNumber(subject.getPhoneNumber());
        if (loginEntryOptional.isPresent()) {
            var loginEntry = loginEntryOptional.get();
            loginEntry.setLastLogin(epochSeconds * 1000);
            loginEntryRepository.save(loginEntry);
        } else
            loginEntryRepository.save(new LoginRecords(subject, epochSeconds * 1000));
        return token;
    }

    public DecodedJWT verifyAndDecodeToken(String token){
        var decodedJwt =  JWT.require(Algorithm.HMAC512(envConstants.getTokenSecret()))
                .build()
                .verify(token);

        var loginEntryOptional = loginEntryRepository
                .findByPhoneNumber(Long.parseLong(decodedJwt.getSubject()));
        if (loginEntryOptional.isEmpty() || decodedJwt.getIssuedAt().getTime() < loginEntryOptional.get().getLastLogin())
            throw new RuntimeException("Invalid Token");

        return decodedJwt;
    }
}