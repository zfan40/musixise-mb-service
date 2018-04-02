package com.musixise.musixisebox.service;

import com.musixise.musixisebox.controller.vo.req.UserReq;

/**
 * Created by zhaowei on 2018/4/1.
 */
public interface UserService {
    String auth(UserReq token);
    UserReq UsernamePasswordAuthenticationToken(String content);
    Long getUserIdByToken(String token);
}
