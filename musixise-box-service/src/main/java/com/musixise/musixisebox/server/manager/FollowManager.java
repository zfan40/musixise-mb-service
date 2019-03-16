package com.musixise.musixisebox.server.manager;

import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.domain.Follow;
import com.musixise.musixisebox.server.domain.QFollow;
import com.musixise.musixisebox.server.repository.FollowRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class FollowManager {

    @Resource FollowRepository followRepository;

    public Map<Long, Follow> getFollows(List<Long> userId) {

        QFollow follow = QFollow.follow;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(follow.userId.eq(MusixiseContext.getCurrentUid()));
        booleanBuilder.and(follow.followId.in(userId));

        Iterable<Follow> repositoryAll = followRepository.findAll(booleanBuilder);
        return StreamSupport.stream(repositoryAll.spliterator(), false).collect(Collectors.toMap(Follow::getFollowId, f -> f));

    }
}
