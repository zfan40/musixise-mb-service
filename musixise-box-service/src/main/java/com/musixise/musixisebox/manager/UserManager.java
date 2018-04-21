package com.musixise.musixisebox.manager;

import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.config.OAuthTypesConstants;
import com.musixise.musixisebox.domain.UserBind;
import com.musixise.musixisebox.repository.UserBindRepository;
import com.musixise.musixisebox.repository.UserRepository;
import com.musixise.musixisebox.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class UserManager {

    @Resource UserService userService;

    @Resource UserRepository userRepository;

    @Resource UserBindRepository userBindRepository;

    @Resource PasswordEncoder passwordEncoder;

    final private static String DEFAULT_PASSWORD="CHANGE_YOU_PASSWORD";

    public String getLoginName(String openId, String provider) {
        return String.format(OAuthTypesConstants.USERNAME, provider, openId);
    }

    @Transactional
    public String createByOauth(SocialVO socialVO) {
        String openId = socialVO.getOpenId();
        String provider = socialVO.getProvider();

        String login = getLoginName(openId, provider);
        String email = String.format("%s@musixise.com", login);

        Register register = new Register();
        register.setUsername(login);
        register.setPassword(DEFAULT_PASSWORD);
        register.setEmail(email);
        register.setSmallAvatar(socialVO.getAvatar());
        register.setLargeAvatar(socialVO.getAvatar());
        register.setBirth("0000-00-00");
        register.setNation("");
        register.setTel("");
        register.setGender("");
        Long userId = userService.register(register);
        if (userId > 0) {
            //bind
            bindThird(openId, login, provider, socialVO.getAccessToken(),
                    socialVO.getRefreshToken(), socialVO.getExpiresIn());
        }

        return login;
    }

    public Boolean bindThird(String openId, String login, String provider, String accessToken, String refreshToken, Integer expiresIn) {
        UserBind userBind = new UserBind();
        userBind.setOpenId(openId);
        userBind.setLogin(login);
        userBind.setProvider(provider);
        if (accessToken != null) {
            userBind.setAccessToken(accessToken);
        } else {
            userBind.setAccessToken("");
        }
        if (refreshToken != null) {
            userBind.setRefreshToken(refreshToken);
        } else {
            userBind.setRefreshToken("");
        }
        if (expiresIn != null) {
            userBind.setExpiresIn(expiresIn);
        }
        UserBind save = userBindRepository.save(userBind);
        if (save.getBid() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
