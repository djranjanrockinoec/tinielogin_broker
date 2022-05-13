package com.tinie.login_broker.config;

import com.tinie.login_broker.exceptions.UserReadResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

@Configuration
public class AppConfig extends WebSecurityConfigurerAdapter{

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf()
                .disable()
                .cors()
                .and()
                .authorizeRequests()
                .anyRequest()
                .permitAll()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Bean
    @Scope(value = "prototype")
    public RestTemplate restTemplate(ResponseErrorHandler errorHandler) {
        return new RestTemplateBuilder()
                .setConnectTimeout(Duration.ofMillis(3000))
                .setReadTimeout(Duration.ofMillis(3000))
                .errorHandler(errorHandler)
                .build();
    }

    @Bean
    @Scope(value = "prototype")
    public UserReadResponseErrorHandler userReadResponseErrorHandler(int OTP, long OTPExpiry, int sessionExpiry) {
        return new UserReadResponseErrorHandler(OTP, OTPExpiry, sessionExpiry);
    }
}
