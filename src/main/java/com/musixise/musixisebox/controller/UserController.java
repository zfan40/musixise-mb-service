package com.musixise.musixisebox.controller;

import com.google.common.base.Preconditions;
import com.musixise.musixisebox.MusixiseException;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.aop.MusixiseContext;
import com.musixise.musixisebox.config.SocialConfiguration;
import com.musixise.musixisebox.controller.vo.req.user.Login;
import com.musixise.musixisebox.controller.vo.req.user.Register;
import com.musixise.musixisebox.controller.vo.req.user.Update;
import com.musixise.musixisebox.controller.vo.resp.JWTToken;
import com.musixise.musixisebox.controller.vo.resp.SocialVO;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.domain.User;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.manager.UserManager;
import com.musixise.musixisebox.repository.UserRepository;
import com.musixise.musixisebox.service.CustomOAuthService;
import com.musixise.musixisebox.service.FollowService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.service.impl.OAuthServices;
import com.musixise.musixisebox.service.impl.SocialService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api/user")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @Resource SocialService socialService;

    @Resource OAuthServices oAuthServices;

    @Resource SocialConfiguration socialConfiguration;

    @Resource UserManager userManager;

    @Resource UserRepository userRepository;

    @Resource FollowService followService;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    @AppMethod
    public ResponseData authorize(Model model,  @Valid Login login) {

        Preconditions.checkArgument(login.getUserName() != null && login.getPassWord() != null);
        String jwt = userService.auth(login);
        return new ResponseData(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }

    @RequestMapping(value = "/detail/{uid}", method = RequestMethod.GET)
    @AppMethod
    public ResponseData getInfo(@Valid @PathVariable Long uid) {
        Preconditions.checkArgument( uid != null &&uid > 0);

        Long currenUid = MusixiseContext.getCurrentUid();
        UserVO userVO = userService.getById(uid);
        if (currenUid > 0) {
            //更新关注装填
            userVO.setFollowNum(followService.isFollow(currenUid, uid) ? 1 : 0);
        } else {
            userVO.setFollowStatus(0);
        }
        return new ResponseData(ExceptionMsg.SUCCESS, userVO);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    @AppMethod
    public ResponseData register(@Valid Register register) {
        Preconditions.checkArgument(register.getUsername() != null && register.getPassword() != null
                && register.getEmail() != null);

        User byLogin = userRepository.findByLogin(register.getUsername());

        if (byLogin != null) {
            return new ResponseData(ExceptionMsg.USERNAME_USED);
        }

        User byEmail = userRepository.findByEmail(register.getEmail());

        if (byEmail != null) {
            return new ResponseData(ExceptionMsg.EMAIL_USED);
        }

        Long id = userService.register(register);
        return new ResponseData(ExceptionMsg.SUCCESS, id);
    }

    @RequestMapping(value = "/updateInfo", method = RequestMethod.PUT)
    @AppMethod(isLogin = true)
    public ResponseData updateInfo(Long uid, @Valid Update update) {
        userService.updateInfo(uid, update);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/authByAccessToken/{platform}", method = RequestMethod.POST)
    @AppMethod
    public ResponseData authenticateBySocialToken(@PathVariable String platform,
                                                  @RequestParam(value = "token", defaultValue = "") String token) {

        AccessGrant accessGrant = new AccessGrant(token);
        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();

        if (oAuth2ConnectionFactoryMap.containsKey(platform)) {
            UserProfile userProfile = null;
            try {
                Connection<?> connection  = oAuth2ConnectionFactoryMap.get(platform).createConnection(accessGrant);
                userProfile = connection.fetchUserProfile();

                //初始化社交账号
                socialService.createSocialConnection(userProfile.getUsername(), connection);

                if (userProfile != null && userProfile.getUsername() != null) {
                    //check user bind info
                    String login = userService.isUserBindThis(userProfile.getUsername(), platform);
                    if (login == null) {
                        return new ResponseData(ExceptionMsg.FAILED);
                    } else {
                        String jwt = userService.getTokenByLogin(login);
                        return new ResponseData(ExceptionMsg.SUCCESS, new JWTToken(jwt));
                    }
                } else {
                    return new ResponseData(ExceptionMsg.FAILED, "create-connection-fail");
                }

            } catch (Exception e) {
                logger.error("Exception creating social user: ", e);
                return new ResponseData(ExceptionMsg.FAILED);
            }
        } else {
            return new ResponseData(ExceptionMsg.PARAM_ERROR);
        }

    }

    @RequestMapping(value = "/oauth/{platform}/callbac", method = RequestMethod.POST)
    @AppMethod
    public ResponseData authenticateBySocialCode(@PathVariable String platform,
                                                 @RequestParam(value = "code", required = true) String code) {
        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();
        if (!oAuth2ConnectionFactoryMap.containsKey(platform)) {
            return new ResponseData(ExceptionMsg.PARAM_ERROR);
        }

        CustomOAuthService oAuthService = oAuthServices.getOAuthService(platform);
        Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));
        if (accessToken != null) {
            SocialVO socialVO = oAuthService.getOAuthUser(accessToken);
            //检查是否已初始化
            if (socialVO != null && socialVO.getOpenId() != null) {
                String login = userManager.getLoginName(socialVO.getOpenId(), socialVO.getProvider());
                if (userService.isUserBindThis(socialVO.getOpenId(), socialVO.getProvider()) == null) {
                    //需要初始化
                    try {
                        login = userManager.createByOauth(socialVO);
                    } catch (Exception e) {
                        throw new MusixiseException("授权异常，请联系客服解决");
                    }
                }

                String jwt = userService.getTokenByLogin(login);
                return new ResponseData(ExceptionMsg.SUCCESS, new JWTToken(jwt));

            } else {
               return new ResponseData(ExceptionMsg.FAILED);
            }

        } else {
            return new ResponseData(ExceptionMsg.FAILED, "accessToken not valid");
        }
    }
}
