package com.quangduong.redis.exception;

import com.quangduong.redis.payload.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<>(
                new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ErrorResponse> noPermissionExceptionHandler(NoPermissionException e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(HttpServletResponse.SC_FORBIDDEN, e.getMessage(), request.getRequestURI()),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(UsernameExistsException.class)
    public ResponseEntity<ErrorResponse> usernameExistsExceptionHandler(UsernameExistsException e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(HttpServletResponse.SC_CONFLICT, e.getMessage(), request.getRequestURI()),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> usernameExistsExceptionHandler(BadCredentialsException e, HttpServletRequest request) {
        logger.error(e.getMessage());
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(HttpServletResponse.SC_BAD_REQUEST, e.getMessage(), request.getRequestURI()),
                HttpStatus.BAD_REQUEST);
    }

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
