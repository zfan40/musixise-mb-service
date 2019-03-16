package com.musixise.musixisebox.server.service;

import com.musixise.musixisebox.server.domain.Musixiser;

import java.util.List;
import java.util.Map;

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

    /**
     * 批量获取用户信息
     * @param userId
     * @return
     */
    public Map<Long, Musixiser> getMusixiserMap(List<Long> userId);
}
