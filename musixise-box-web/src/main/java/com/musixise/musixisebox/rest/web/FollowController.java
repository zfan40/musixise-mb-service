package com.musixise.musixisebox.rest.web;

import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.FollowApi;
import com.musixise.musixisebox.api.web.vo.req.follow.CreateFollowVO;
import com.musixise.musixisebox.api.web.vo.resp.follow.FollowVO;
import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.domain.Follow;
import com.musixise.musixisebox.server.repository.FollowRepository;
import com.musixise.musixisebox.server.service.FollowService;
import com.musixise.musixisebox.server.service.MusixiseService;
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
@RequestMapping("/api/v1/follow")
public class FollowController implements FollowApi {

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
    @AppMethod
    @Override
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
    @AppMethod
    @Override
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
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<Void> add(Long uid, @Valid @RequestBody CreateFollowVO createFollowVO){

        uid = MusixiseContext.getCurrentUid();
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
