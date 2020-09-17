package com.thoughtworks.rslist.exceptions;

import lombok.AllArgsConstructor;

public class InvalidParamException extends CommonException {
    public InvalidParamException(String message) {
        super(message);
    }
}
