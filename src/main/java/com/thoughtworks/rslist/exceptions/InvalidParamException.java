package com.thoughtworks.rslist.exceptions;

import lombok.AllArgsConstructor;

public class InvalidParamException extends Exception {
    public InvalidParamException(String message) {
        super(message);
    }
}
