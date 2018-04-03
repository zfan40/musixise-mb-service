package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.req.user.Login;
import com.musixise.musixisebox.controller.vo.req.user.Register;
import com.musixise.musixisebox.controller.vo.req.user.Update;
import com.musixise.musixisebox.controller.vo.resp.JWTToken;
import com.musixise.musixisebox.controller.vo.resp.UserDetail;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @AppMethod
    public ResponseData authorize(Model model,  @Valid Login login) {

        String jwt = userService.auth(login);
        return new ResponseData(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @AppMethod
    public ResponseData getInfo(@PathVariable Long uid) {
        UserDetail userDetail = userService.getById(uid);
        return new ResponseData(ExceptionMsg.SUCCESS, userDetail);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @AppMethod
    public ResponseData register(@Valid Register register) {
        Long id = userService.register(register);
        return new ResponseData(ExceptionMsg.SUCCESS, id);
    }

    @RequestMapping(value = "/updateInfo", method = RequestMethod.PUT)
    @AppMethod(isLogin = true)
    public ResponseData updateInfo(Long uid, @Valid Update update) {
        userService.updateInfo(uid, update);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }



}
