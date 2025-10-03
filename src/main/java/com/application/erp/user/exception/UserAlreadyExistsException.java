package com.application.erp.user.exception;

import lombok.Getter;

@Getter
public class UserAlreadyExistsException extends RuntimeException {

    private final String field;

    public UserAlreadyExistsException(String message, String field) {
        super(message);
        this.field = field;
    }
}
