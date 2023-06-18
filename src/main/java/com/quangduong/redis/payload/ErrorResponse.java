package com.quangduong.redis.payload;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ErrorResponse {

    private String timestamp;
    private int status;
    private String error;
    private String path;

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public ErrorResponse(int status, String error, String path) {
        this.timestamp = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ").format(new Date());
        this.status = status;
        this.error = error;
        this.path = path;
    }
}
