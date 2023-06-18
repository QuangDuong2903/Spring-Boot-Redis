package com.quangduong.redis.exception;

import com.quangduong.redis.payload.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> validationExceptionHandler(BindException e, HttpServletRequest request) {
        logger.error("BindException: " + e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return ResponseEntity.badRequest().body(new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST,
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                request.getRequestURI()));
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<ErrorResponse> globalExceptionHandler(Exception e, HttpServletRequest request) {
//        logger.error("Exception: ", e.getCause());
//        return ResponseEntity.internalServerError().body(new ErrorResponse(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
//                "INTERNAL SERVER ERROR", request.getRequestURI()));
//    }
}
