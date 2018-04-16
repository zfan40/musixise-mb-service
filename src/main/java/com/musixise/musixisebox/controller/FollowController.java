package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.req.follow.CreateFollowVO;
import com.musixise.musixisebox.controller.vo.resp.follow.FollowVO;
import com.musixise.musixisebox.domain.Follow;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixisePageResponse;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.repository.FollowRepository;
import com.musixise.musixisebox.service.FollowService;
import com.musixise.musixisebox.service.MusixiseService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/4.
 */
@RestController
@Api(value = "用户关注", description = "用户关注", tags = "用户关注")
@RequestMapping("/api/v1/follow")
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
    @RequestMapping(value = "/followings/{uid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取关注列表")
    @AppMethod
    public MusixisePageResponse<List<FollowVO>> getFollowingList(@PathVariable Long uid,
                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                             @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Follow> follows = followRepository.findAllByUserId(pageable, uid);

        List<FollowVO> followVOList = new ArrayList<>();
        follows.forEach( follow -> {
            FollowVO followVO = new FollowVO();
            followService.getFollowByUserId(follow.getUserId());
            followVOList.add(followVO);
        });

        return new MusixisePageResponse<>(followVOList, follows.getTotalElements(), size, page);
    }


    /**
     * 获取粉丝列表
     * @param uid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/followers/{uid}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "获取粉丝列表")
    @AppMethod
    public MusixisePageResponse<List<FollowVO>> getFollowers(@PathVariable Long uid,
                                         @RequestParam(value = "page", defaultValue = "1") Integer page,
                                         @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Follow> follows = followRepository.findAllByFollowId(pageable, uid);

        List<FollowVO> followVOList = new ArrayList<>();
        follows.forEach( follow -> {
            FollowVO followVO = new FollowVO();
            followService.getFollowByUserId(follow.getUserId());
            followVOList.add(followVO);
        });

        return new MusixisePageResponse<>(followVOList, follows.getTotalElements(), size, page);
    }

    /**
     * 关注用户
     * @param uid
     * @param status 1=关注，0=解除关注
     * @return
     */
    @RequestMapping(value = "/add", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "关注用户")
    @AppMethod(isLogin = true)
    @ApiImplicitParam(name = "status", value = "关注状态 (1=关注，0=解除关注)", defaultValue = "1", allowableValues="0,1", dataType = "Integer")
    public MusixiseResponse<Void> add(Long uid, @Valid @RequestBody CreateFollowVO createFollowVO){

        Long followId = createFollowVO.getFollowId();
        Integer status = Optional.ofNullable(createFollowVO.getStatus()).orElse(1);

        if (status == 1) {
            //检查是否已经关注
            followService.add(uid, followId);
        } else if(status == 0) {
            followService.cancel(uid, followId);
        } else {
            return new MusixiseResponse<>(ExceptionMsg.PARAM_ERROR);
        }
        //更新计数器
        musixiseService.updateFollowCount(uid, followId);
        return MusixiseResponse.successResponse("ok");
    }
}
