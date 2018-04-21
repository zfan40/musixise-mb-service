package com.musixise.musixisebox.api.web.service;

import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.req.user.Login;
import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.req.user.Update;
import com.musixise.musixisebox.api.web.vo.resp.JWTToken;
import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "用户接口", description = "用户接口描述", tags = "用户常用接口")
public interface UserApi {

    @ApiOperation(value = "授权接口",notes = "获取 accesstoken")
    MusixiseResponse<JWTToken> authorize(Model model, @Valid @RequestBody Login login);

    @ApiOperation(value = "获取用户详细信息", notes = "通过传入用户ID ")
    MusixiseResponse<UserVO> getInfo(@Valid @PathVariable Long uid);

    @ApiOperation(value = "获取当前用户信息")
    MusixiseResponse<UserVO> getCuurentUserInfo(Long uid);

    @ApiOperation(value = "注册一个账号", notes = "注册成功，返回用户ID")
    MusixiseResponse<Long> register(@Valid @RequestBody Register register);

    @ApiOperation(value = "更新用户信息", notes = "需要登录")
    MusixiseResponse<Void> updateInfo(Long uid, @Valid @RequestBody Update update);

    MusixiseResponse authenticateBySocialToken(@PathVariable String platform,
                                                      @RequestParam(value = "token", defaultValue = "") String token);


    @ApiOperation(value = "社交账号 OAUTH ", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "社交平台 wechat, weibo",  required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "code", required = true, dataTypeClass = String.class)
    })
    MusixiseResponse<JWTToken> authenticateBySocialCode(@PathVariable String platform,
                                                               @RequestParam(value = "code", required = true) String code);
}
