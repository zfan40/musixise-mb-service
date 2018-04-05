package com.musixise.musixisebox.service;

import com.musixise.musixisebox.controller.vo.resp.follow.FollowVO;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by zhaowei on 2018/4/3.
 */
public interface FollowService {

    /**
     * 获取用户关注列表
     * @param userId
     * @return
     */
    FollowVO getFollowByUserId(Long userId);

    /**
     * 获取粉丝列表
     * @param userId
     * @param pageable
     * @return
     */
    List<FollowVO> getFollowerByUserId(Long userId, Pageable pageable);

    /**
     * 添加关注
     * @param uid
     * @param followId
     */
    Boolean add(Long uid, Long followId);

    /**
     * 取消关注
     * @param uid
     * @param followId
     * @return
     */
    Boolean cancel(Long uid, Long followId);

    /**
     * 是否关注
     * @param uid
     * @param followId
     * @return
     */
    Boolean isFollow(Long uid, Long followId);
}
