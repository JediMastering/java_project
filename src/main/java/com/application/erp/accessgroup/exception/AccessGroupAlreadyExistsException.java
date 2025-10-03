package com.application.erp.accessgroup.exception;

import lombok.Getter;

@Getter
public class AccessGroupAlreadyExistsException extends RuntimeException {

    private final String field;

    public AccessGroupAlreadyExistsException(String message, String field) {
        super(message);
        this.field = field;
    }
}
