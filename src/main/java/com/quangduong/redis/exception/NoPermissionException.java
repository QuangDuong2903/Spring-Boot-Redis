package com.quangduong.redis.exception;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException(String message) {
        super(message);
    }
}
