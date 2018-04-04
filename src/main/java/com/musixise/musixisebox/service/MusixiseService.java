package com.musixise.musixisebox.service;

/**
 * Created by zhaowei on 2018/4/2.
 */
public interface MusixiseService {
    /**
     * 更新关注数据
     * @param uid
     * @param followId
     */
    public void updateFollowCount(Long uid, Long followId);
}
