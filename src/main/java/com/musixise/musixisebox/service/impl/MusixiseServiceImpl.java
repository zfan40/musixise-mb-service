package com.musixise.musixisebox.service.impl;

import com.musixise.musixisebox.repository.FollowRepository;
import com.musixise.musixisebox.repository.MusixiserRepository;
import com.musixise.musixisebox.repository.WorkRepository;
import com.musixise.musixisebox.service.MusixiseService;
import org.springframework.scheduling.annotation.Async;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by zhaowei on 2018/4/4.
 */
public class MusixiseServiceImpl implements MusixiseService {

    @Resource MusixiserRepository musixiserRepository;

    @Resource FollowRepository followRepository;

    @Resource WorkRepository workRepository;

    @Override
    @Transactional
    @Async
    public void updateFollowCount(Long uid, Long followId) {

        Integer followNum = followRepository.countByUserId(uid);
        //更新关注数
        musixiserRepository.updateFollowNumById(uid, followNum);
        Integer followingNum = followRepository.countByFollowId(followId);
        //更新粉丝数
        musixiserRepository.updateFanswNumById(followId, followingNum);
    }

    @Override
    @Async
    @Transactional
    public void updateWorkCount(Long uid) {
        int workCnt = workRepository.countByUserId(uid);
        musixiserRepository.updateWorkNumById(uid, workCnt);
    }
}
