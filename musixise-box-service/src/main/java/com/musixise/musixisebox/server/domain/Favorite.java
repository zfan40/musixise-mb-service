package com.musixise.musixisebox.server.domain;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Entity
@Table(name = "mu_work_list_follow")
public class Favorite extends AbstractEntity implements Serializable {

    private static final long serialVersionUID = 5414810587282134536L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "work_id")
    @NotNull
    private Long workId;

    @Column(name = "user_id")
    @NotNull
    private Long userId;

    public Favorite(@NotNull Long userId, @NotNull Long workId) {
        this.workId = workId;
        this.userId = userId;
    }

    public Favorite() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getWorkId() {
        return workId;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
