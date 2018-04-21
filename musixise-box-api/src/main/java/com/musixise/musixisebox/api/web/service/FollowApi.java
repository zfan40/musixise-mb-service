package com.musixise.musixisebox.api.web.service;

import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.req.follow.CreateFollowVO;
import com.musixise.musixisebox.api.web.vo.resp.follow.FollowVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "用户关注", description = "用户关注", tags = "用户关注")
public interface FollowApi {

    @ApiOperation(value = "获取关注列表")
    MusixisePageResponse<List<FollowVO>> getFollowingList(@PathVariable Long uid,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size);

    @ApiOperation(value = "获取粉丝列表")
    MusixisePageResponse<List<FollowVO>> getFollowers(@PathVariable Long uid,
                                                             @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                             @RequestParam(value = "size", defaultValue = "10") Integer size);

    @ApiOperation(value = "关注用户")
    @ApiImplicitParam(name = "status", value = "关注状态 (1=关注，0=解除关注)", defaultValue = "1", allowableValues="0,1", dataType = "Integer")
    MusixiseResponse<Void> add(Long uid, @Valid @RequestBody CreateFollowVO createFollowVO);
}