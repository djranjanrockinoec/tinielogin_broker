package com.tinie.login_broker.dtos.responses;

import lombok.Data;

/**
 * A DTO that is returned every time one of our custom Exceptions is thrown in the app.
 * */
@Data
public class ActionResponse {
        private long phonenumber;
        private String action;
}
