package com.musixise.musixisebox.server.service.impl;

import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.domain.QMusixiser;
import com.musixise.musixisebox.server.repository.FollowRepository;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.MusixiseService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by zhaowei on 2018/4/4.
 */
@Component
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

    @Override
    public Map<Long, Musixiser> getMusixiserMap(List<Long> userIds) {
        QMusixiser musixiser = QMusixiser.musixiser;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(musixiser.userId.in(userIds));
        Iterable<Musixiser> musixisersIt = musixiserRepository.findAll(booleanBuilder);
        return StreamSupport.stream(musixisersIt.spliterator(), false).collect(Collectors.toMap(Musixiser::getUserId, m -> m));
    }
}
