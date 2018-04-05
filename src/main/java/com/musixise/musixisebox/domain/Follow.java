package com.musixise.musixisebox.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 2018/4/4.
 */
@Entity
@Table(name = "mu_musixiser_follow")
public class Follow extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = -6687709261905352491L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long userId;

    @NotNull
    private Long followId;

    public Follow() {
    }

    public Follow(@NotNull Long userId, @NotNull Long followId) {
        this.userId = userId;
        this.followId = followId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFollowId() {
        return followId;
    }

    public void setFollowId(Long followId) {
        this.followId = followId;
    }
}
