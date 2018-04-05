package com.musixise.musixisebox.controller;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.resp.follow.FollowVO;
import com.musixise.musixisebox.domain.Follow;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.repository.FollowRepository;
import com.musixise.musixisebox.service.FollowService;
import com.musixise.musixisebox.service.MusixiseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/4.
 */
@RestController
@RequestMapping("/api/follow")
public class FollowController {

    @Resource FollowRepository followRepository;

    @Resource FollowService followService;

    @Resource MusixiseService musixiseService;

    /**
     * 获取关注列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/followings/{uid}", method = RequestMethod.GET)
    @AppMethod
    public MusixiseResponse getFollowingList(@PathVariable Long uid,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        Page<Follow> follows = followRepository.findAllByUserId(pageable, uid);

        List<FollowVO> followVOList = new ArrayList<>();
        follows.forEach( follow -> {
            FollowVO followVO = new FollowVO();
            followService.getFollowByUserId(follow.getUserId());
            followVOList.add(followVO);
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", follows.getTotalElements());
        jsonObject.put("list", followVOList);
        jsonObject.put("size", size);
        jsonObject.put("current", page);

        return new MusixiseResponse(ExceptionMsg.SUCCESS, jsonObject);
    }


    /**
     * 获取粉丝列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/followers/{uid}")
    @AppMethod
    public MusixiseResponse getFollowers(@PathVariable Long uid,
                                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        Page<Follow> follows = followRepository.findAllByFollowId(pageable, uid);

        List<FollowVO> followVOList = new ArrayList<>();
        follows.forEach( follow -> {
            FollowVO followVO = new FollowVO();
            followService.getFollowByUserId(follow.getUserId());
            followVOList.add(followVO);
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", follows.getTotalElements());
        jsonObject.put("list", followVOList);
        jsonObject.put("size", size);
        jsonObject.put("current", page);

        return new MusixiseResponse(ExceptionMsg.SUCCESS, jsonObject);
    }

    /**
     * 关注用户
     * @param uid
     * @param status 1=关注，0=解除关注
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    public MusixiseResponse add(Long uid, @RequestParam(value = "followId", defaultValue = "0") Long followId ,
                                @RequestParam(value = "status", defaultValue = "1") Integer status) {

        if (status == 1) {
            //检查是否已经关注
            followService.add(uid, followId);
        } else if(status == 0) {
            followService.cancel(uid, followId);
        } else {
            return new MusixiseResponse(ExceptionMsg.PARAM_ERROR);
        }
        //更新计数器
        musixiseService.updateFollowCount(uid, followId);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }
}
