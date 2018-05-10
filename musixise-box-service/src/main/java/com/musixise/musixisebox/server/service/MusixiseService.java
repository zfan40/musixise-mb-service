package com.musixise.musixisebox.server.service;

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

    /**
     * 更新作品数量
     * @param uid
     */
    public void updateWorkCount(Long uid);
}
