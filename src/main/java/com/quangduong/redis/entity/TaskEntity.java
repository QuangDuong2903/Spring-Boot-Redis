package com.quangduong.redis.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "task")
public class TaskEntity extends BaseEntity {

    @Column(name = "title")
    private String title;

    @Column(name = "done")
    private boolean done;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public TaskEntity() {}

    public TaskEntity(String title, boolean done, UserEntity user) {
        this.title = title;
        this.done = done;
        this.user = user;
    }
}
