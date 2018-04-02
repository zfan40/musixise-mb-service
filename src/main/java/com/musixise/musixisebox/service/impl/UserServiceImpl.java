package com.musixise.musixisebox.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.musixise.musixisebox.MusixiseException;
import com.musixise.musixisebox.controller.vo.req.UserReq;
import com.musixise.musixisebox.domain.User;
import com.musixise.musixisebox.repository.UserRepository;
import com.musixise.musixisebox.security.jwt.TokenProvider;
import com.musixise.musixisebox.service.UserService;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
@Component
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserRepository userRepository;

    @Resource TokenProvider tokenProvider;

    @Resource PasswordEncoder passwordEncoder;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String auth(UserReq userReq) {
        Preconditions.checkNotNull(userReq);
        Optional<User> oneByLogin = userRepository.findOneByLogin(userReq.getUserName());
        return oneByLogin.map(user -> {
            if (!user.isActivated()) {
                logger.warn("user no active");
                throw new MusixiseException("用户未激活");
            }
            //check pwd
            if (passwordEncoder.matches(userReq.getPassWord(), user.getPassword())) {
                String onece = String.valueOf(System.currentTimeMillis());
                return tokenProvider.createToken(onece, user.getId(), 3600*24*30*1000L);
            } else {
                logger.warn("user auth fail");
                throw new MusixiseException("用户验证失败");
            }
        }).orElseThrow(() -> new MusixiseException("不存在的用户"));
    }

    @Override
    public UserReq UsernamePasswordAuthenticationToken(String content) {
        Preconditions.checkNotNull(content);

        return JSON.parseObject(content, UserReq.class);

    }

    @Override
    public Long getUserIdByToken(String token) {
        Preconditions.checkArgument(token != null);
        Claims claims = null;

        try {
            claims = tokenProvider.parseJWT(token);
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        }

        //check expired
        return Long.valueOf(claims.getOrDefault("uid", 0).toString());
    }
}

