package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.ErrorDto;
import com.thoughtworks.rslist.exceptions.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalController {

    @ExceptionHandler({
            InvalidRequestParamException.class,
            InvalidIndexException.class
    })
    public ResponseEntity handleException(Exception e) {
        return ResponseEntity.status(400).body(new ErrorDto(e.getMessage()));
    }
}
