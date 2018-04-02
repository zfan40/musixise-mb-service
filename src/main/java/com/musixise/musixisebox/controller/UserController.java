package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.req.UserReq;
import com.musixise.musixisebox.controller.vo.resp.JWTToken;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @AppMethod
    public ResponseData authorize(Model model,  @Valid UserReq userReq) {

        String jwt = userService.auth(userReq);
        return new ResponseData(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }

    @RequestMapping(value = "/get", method = RequestMethod.GET)
    @AppMethod(isLogin = true)
    public ResponseData get(Long uid) {
        return new ResponseData(ExceptionMsg.SUCCESS, uid);
    }
}
