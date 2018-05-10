package com.musixise.musixisebox.server.service;

import com.musixise.musixisebox.api.web.vo.req.user.Login;
import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.req.user.Update;
import com.musixise.musixisebox.api.web.vo.resp.UserVO;

/**
 * Created by zhaowei on 2018/4/1.
 */
public interface UserService {
    String auth(Login login);
    Login UsernamePasswordAuthenticationToken(String content);
    Long getUserIdByToken(String token);
    Long register(Register register);
    void updateInfo(Long uid, Update update);
    UserVO getById(Long uid);
    UserVO getById(Long uid, Boolean exception);

    String isUserBindThis(String openId, String provider);

    String getTokenByLogin(String login);
}
