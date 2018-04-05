package com.musixise.musixisebox.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Preconditions;
import com.musixise.musixisebox.MusixiseException;
import com.musixise.musixisebox.controller.vo.req.user.Login;
import com.musixise.musixisebox.controller.vo.req.user.Register;
import com.musixise.musixisebox.controller.vo.req.user.Update;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.domain.User;
import com.musixise.musixisebox.domain.UserBind;
import com.musixise.musixisebox.repository.MusixiserRepository;
import com.musixise.musixisebox.repository.UserBindRepository;
import com.musixise.musixisebox.repository.UserRepository;
import com.musixise.musixisebox.security.jwt.TokenProvider;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.transfter.UserTransfter;
import com.musixise.musixisebox.utils.CommonUtil;
import io.jsonwebtoken.Claims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
@Component
public class UserServiceImpl implements UserService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserRepository userRepository;

    @Resource MusixiserRepository musixiserRepository;

    @Resource UserBindRepository userBindRepository;

    @Resource TokenProvider tokenProvider;

    @Resource PasswordEncoder passwordEncoder;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public String auth(Login login) {
        Preconditions.checkNotNull(login);
        Optional<User> oneByLogin = userRepository.findByLoginOrEmail(login.getUserName(), login.getUserName());
        return oneByLogin.map(user -> {
            if (!user.isActivated()) {
                logger.warn("user no active");
                throw new MusixiseException("用户未激活");
            }
            //check pwd
            if (passwordEncoder.matches(login.getPassWord(), user.getPassword())) {
                String onece = String.valueOf(System.currentTimeMillis());
                return tokenProvider.createToken(onece, user.getId(), 3600*24*30*1000L);
            } else {
                logger.warn("user auth fail");
                throw new MusixiseException("用户验证失败");
            }
        }).orElseThrow(() -> new MusixiseException("不存在的用户"));
    }

    @Override
    public Login UsernamePasswordAuthenticationToken(String content) {
        Preconditions.checkNotNull(content);

        return JSON.parseObject(content, Login.class);

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

    @Override
    @Transactional
    public Long register(Register register) {
        User user = new User();
        user.setLogin(register.getUsername());
        user.setPassword(register.getPassword());
        user.setActivated(true);
        user.setEmail(register.getEmail());
        userRepository.save(user);

        Musixiser musixiser = new Musixiser();
        CommonUtil.copyPropertiesIgnoreNull(register, musixiser);
        Musixiser save = musixiserRepository.save(musixiser);
        Preconditions.checkNotNull(save);
        return user.getId();
    }

    @Override
    public void updateInfo(Long uid, Update update) {
        Optional<Musixiser> musixiser = musixiserRepository.findOneByUserId(uid);
        musixiser.map(mu -> {
            CommonUtil.copyPropertiesIgnoreNull(update, mu);
            return musixiserRepository.save(mu);
        }).orElseThrow(() -> new MusixiseException("不存在的用户"));
    }

    @Override
    public UserVO getById(Long uid) {

        Optional<Musixiser> musixiser = musixiserRepository.findOneByUserId(uid);
        musixiser.map(mu -> {
            return UserTransfter.getUserDetail(mu);

        }).orElseThrow(() -> new MusixiseException("不存在的用户"));

        return null;
    }

    /**
     * 检测社交账号是否已绑定
     * @param openId
     * @return
     */
    @Override
    public String isUserBindThis(String openId, String provider) {
        UserBind userBind = userBindRepository.findByOpenIdAndProvider(openId, provider);
        if (userBind != null && userBind.getLogin() != null) {
            return userBind.getLogin();
        }
        return null;
    }

    @Override
    public String getTokenByLogin(String login) {
        Optional<User> user = userRepository.findOneByLogin(login);
        user.map(u -> {
            String onece = String.valueOf(System.currentTimeMillis());
            return tokenProvider.createToken(onece, u.getId(), 3600*24*30*1000L);
        }).orElseThrow(() -> new MusixiseException("不存在的用户"));
        return null;
    }
}

