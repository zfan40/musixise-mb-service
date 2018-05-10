package com.musixise.musixisebox.server.service.impl;

import com.musixise.musixisebox.api.web.vo.resp.follow.FollowVO;
import com.musixise.musixisebox.server.domain.Follow;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.repository.FollowRepository;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.service.FollowService;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/4.
 */
@Component("followServiceImpl")
public class FollowServiceImpl implements FollowService {

    @Resource FollowRepository followRepository;

    @Resource MusixiserRepository musixiserRepository;

    @Override
    public FollowVO getFollowByUserId(Long userId) {

        List<FollowVO> followVOList = new ArrayList<>();

        Optional<Musixiser> musixiser = musixiserRepository.findOneByUserId(userId);
        FollowVO followVO = new FollowVO();
        if (musixiser.isPresent()) {
            followVO.setId(userId);
            followVO.setUserId(musixiser.get().getUserId());
            followVO.setSmallAvatar(musixiser.get().getSmallAvatar());
            followVO.setLargeAvatar(musixiser.get().getLargeAvatar());
        }
        return followVO;
    }

    @Override
    public List<FollowVO> getFollowerByUserId(Long userId, Pageable pageable) {
        return null;
    }

    @Override
    public Boolean add(Long uid, Long followId) {
        Follow follow = followRepository.findByUserIdAndFollowUid(uid, followId);
        if (follow != null) {
            return false;
        } else {
            followRepository.save(new Follow(uid, followId));
            return true;
        }
    }

    @Override
    public Boolean cancel(Long uid, Long followId) {
        Follow follow = followRepository.findByUserIdAndFollowUid(uid, followId);
        if (follow != null) {
            followRepository.delete(follow);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Boolean isFollow(Long uid, Long followId) {
        Follow follow = followRepository.findByUserIdAndFollowUid(uid, followId);
        if (follow == null) {
            return false;
        } else {
            return false;
        }
    }
}
