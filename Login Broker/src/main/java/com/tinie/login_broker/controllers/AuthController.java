package com.tinie.login_broker.controllers;

import com.tinie.login_broker.dtos.requests.VerifyTokenRequest;
import com.tinie.login_broker.dtos.responses.ActionResponse;
import com.tinie.login_broker.dtos.responses.InvalidOTPResponse;
import com.tinie.login_broker.dtos.responses.OTPGenResponse;
import com.tinie.login_broker.dtos.responses.TokenResponse;
import com.tinie.login_broker.services.AuthService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.catalina.connector.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("api/v1")
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Generates and sends OTP to phone number and returns associated user details
     * @param httpServletRequest An object of type {@link HttpServletRequest} containing all the information about the request.
     * @param phoneNumber Valid phone number.
     * @return A {@link Response} whose payload is an {@link OTPGenResponse}.
     * */
    @GetMapping("login-broker")
    @ApiOperation(value = "Trigger OTP API and Read USER details API, then return user information.")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = OTPGenResponse.class),
            @ApiResponse(code = 500, message = "OTP GEN OR USER READ FAILED", response = ActionResponse.class),
            @ApiResponse(code = 404, message = "USER DETAILS NOT FOUND", response = OTPGenResponse.class)

    })
    public ResponseEntity<OTPGenResponse> genOTPReadUser(
            HttpServletRequest httpServletRequest, @RequestParam("phonenumber") long phoneNumber){

        Map<String, Object> dataMap = authService.genOTPReadUser(phoneNumber);

        OTPGenResponse response = new OTPGenResponse();
        response.setPhonenumber(phoneNumber);
        response.setOTP((Integer) dataMap.get("otp"));
        response.setOTPExpiry((Long) dataMap.get("otpExpiry"));
        response.setAction((String) dataMap.get("action"));
        response.setUsername((String) dataMap.get("username"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Verify if otp and return auth token if valid
     * @param httpServletRequest An object of type {@link HttpServletRequest} containing all the information about the request.
     * @param otp OTP to be verified
     * @param phoneNumber phone number associated with otp
     * @return A {@link Response} whose payload is an {@link TokenResponse}.
     * */
    @GetMapping("get-token")
    @ApiOperation(value = "Verify OTP and return auth token if valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = TokenResponse.class),
            @ApiResponse(code = 400, message = "INVALID OTP", response = InvalidOTPResponse.class)

    })
    public ResponseEntity<TokenResponse> verifyOTPGetToken(
            HttpServletRequest httpServletRequest,
            @RequestParam("otp") int otp,
            @RequestParam("phonenumber") long phoneNumber){

        Map<String, Object> dataMap = authService.verifyOTP(phoneNumber, otp);

        TokenResponse response = new TokenResponse();
        response.setPhonenumber(phoneNumber);
        response.setSessionExpiry((Integer) dataMap.get("sessionExpiry"));
        response.setUsername((String) dataMap.get("username"));
        response.setToken((String) dataMap.get("token"));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * Verify token given is valid and matches given phone number
     * @param httpServletRequest An object of type {@link HttpServletRequest} containing all the information about the request.
     * @param requestBody {@link VerifyTokenRequest} containing phone number and token
     * @return A {@link Response} whose payload is an {@link ActionResponse}.
     * */
    @PostMapping("verify-token")
    @ApiOperation(value = "Verify Token still valid")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "TOKEN OK"),
            @ApiResponse(code = 400, message = "INVALID TOKEN")
    })
    public ResponseEntity<ActionResponse> verifyToken(HttpServletRequest httpServletRequest,
                                                      @RequestBody VerifyTokenRequest requestBody) {
        var isTokenValid = authService
                .verifyJWT(String.valueOf(requestBody.getPhonenumber()), requestBody.getToken());

        var response = new ActionResponse();
        response.setAction(isTokenValid ? "TOKENOK" : "TOKENNOK");
        response.setPhonenumber(requestBody.getPhonenumber());

        return new ResponseEntity<>(response, isTokenValid ? HttpStatus.OK : HttpStatus.BAD_REQUEST);
    }
}
