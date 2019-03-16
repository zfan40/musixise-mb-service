package com.musixise.musixisebox.server.manager;

import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.server.config.OAuthTypesConstants;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.domain.QMusixiser;
import com.musixise.musixisebox.server.domain.User;
import com.musixise.musixisebox.server.domain.UserBind;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.repository.UserBindRepository;
import com.musixise.musixisebox.server.repository.UserRepository;
import com.musixise.musixisebox.server.service.UserService;
import com.querydsl.core.BooleanBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class UserManager {

    @Resource UserService userService;

    @Resource UserRepository userRepository;

    @Resource UserBindRepository userBindRepository;

    @Resource MusixiserRepository musixiserRepository;

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
        register.setRealname(socialVO.getNickName());
        register.setSmallAvatar(socialVO.getAvatar());
        register.setLargeAvatar(socialVO.getAvatar());
        register.setBirth("2000-01-01");
        register.setNation("");
        register.setTel("");
        register.setGender("");

        Long register1 = userService.register(register);
        Long userId = register1;
        if (userId > 0) {
            //bind
            bindThird(userId, openId, login, provider, socialVO.getAccessToken(),
                    socialVO.getRefreshToken(), socialVO.getExpiresIn());
        }

        return login;
    }

    @Transactional
    public void cleanAccount(String login) {
        //query
        User byLoginExist = userRepository.findByLogin(login);
        if (byLoginExist != null) {
            //clean exist data
            musixiserRepository.deleteByUserId(byLoginExist.getId());
            userRepository.deleteById(byLoginExist.getId());
        }
    }

    public Boolean bindThird(Long userId, String openId, String login, String provider, String accessToken, String refreshToken, Integer expiresIn) {
        UserBind userBind = new UserBind();
        userBind.setOpenId(openId);
        userBind.setLogin(login);
        userBind.setProvider(provider);
        userBind.setUserId(userId);
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


    public Map<Long, Musixiser> getMusixiserMap(List<Long> userIds) {
        QMusixiser musixiser = QMusixiser.musixiser;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(musixiser.userId.in(userIds));
        Iterable<Musixiser> musixisersIt = musixiserRepository.findAll(booleanBuilder);
        return StreamSupport.stream(musixisersIt.spliterator(), false).collect(Collectors.toMap(Musixiser::getUserId, m -> m));
    }
}
