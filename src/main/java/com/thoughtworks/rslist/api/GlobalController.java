package com.thoughtworks.rslist.api;

import com.thoughtworks.rslist.dto.ErrorDto;
import com.thoughtworks.rslist.exceptions.CommonException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalController {

    Logger logger = LoggerFactory.getLogger(GlobalController.class);

    @ExceptionHandler({
            CommonException.class
    })
    public ResponseEntity handleException(Exception e) {
        logger.error(e.getMessage());
        return ResponseEntity.status(400).body(new ErrorDto(e.getMessage()));
    }
}
