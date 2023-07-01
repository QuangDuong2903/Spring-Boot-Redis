package com.quangduong.redis.dto.todo;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public class TaskDTO implements Serializable {

    private long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotNull(message = "status is required")
    private Boolean done;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Boolean getDone() {
        return done;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public TaskDTO() {}

    public TaskDTO(long id, String title, Boolean done) {
        this.id = id;
        this.title = title;
        this.done = done;
    }
}
