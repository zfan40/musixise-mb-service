package com.musixise.musixisebox.rest.web;

import com.google.common.base.Preconditions;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.exception.MusixiseException;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.UserApi;
import com.musixise.musixisebox.api.web.vo.req.user.Login;
import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.req.user.Update;
import com.musixise.musixisebox.api.web.vo.resp.JWTToken;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.server.aop.AppMethod;
import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.config.SocialConfiguration;
import com.musixise.musixisebox.server.domain.User;
import com.musixise.musixisebox.server.manager.UserManager;
import com.musixise.musixisebox.server.repository.UserRepository;
import com.musixise.musixisebox.server.service.CustomOAuthService;
import com.musixise.musixisebox.server.service.FollowService;
import com.musixise.musixisebox.server.service.UserService;
import com.musixise.musixisebox.server.service.impl.OAuthServices;
import com.musixise.musixisebox.server.service.impl.SocialService;
import org.scribe.model.Token;
import org.scribe.model.Verifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.oauth2.AccessGrant;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Map;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController implements UserApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @Resource SocialService socialService;

    @Resource OAuthServices oAuthServices;

    @Resource SocialConfiguration socialConfiguration;

    @Resource UserManager userManager;

    @Resource UserRepository userRepository;

    @Resource FollowService followService;

    @RequestMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse<JWTToken> authorize(Model model, @Valid @RequestBody Login login) {

        Preconditions.checkArgument(login.getUserName() != null && login.getPassWord() != null, "请输入用户名和密码");
        String jwt = userService.auth(login);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }

    @RequestMapping(value = "/detail/{uid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @AppMethod
    @Override
    public MusixiseResponse<UserVO> getInfo(@Valid @PathVariable Long uid) {
        Preconditions.checkArgument( uid != null &&uid > 0, "请填写正确的用户ID");

        Long currenUid = MusixiseContext.getCurrentUid();
        UserVO userVO = userService.getById(uid);
        if (currenUid > 0) {
            //更新关注装填
            userVO.setFollowStatus(followService.isFollow(currenUid, uid) ? 1 : 0);
        } else {
            userVO.setFollowStatus(0);
        }
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, userVO);
    }

    @RequestMapping(value = "/getInfo", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<UserVO> getCuurentUserInfo(Long uid) {
        UserVO userVO = Optional.ofNullable(userService.getById(MusixiseContext.getCurrentUid())).orElseThrow(() -> new MusixiseException("不存在的用户"));
        return new MusixiseResponse<>(userVO);
    }

    @RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse<Long> register(@Valid @RequestBody Register register) {
        Preconditions.checkArgument(register.getUsername() != null && register.getPassword() != null,
                "请填写用户和密码", register.getUsername(), register.getPassword());

        Preconditions.checkArgument(register.getEmail() != null, "邮箱不能为空", register.getEmail());

        User byLogin = userRepository.findByLogin(register.getUsername());

        if (byLogin != null) {
            return new MusixiseResponse<>(ExceptionMsg.USERNAME_USED);
        }

        User byEmail = userRepository.findByEmail(register.getEmail());

        if (byEmail != null) {
            return new MusixiseResponse<>(ExceptionMsg.EMAIL_USED);
        }

        Long id = userService.register(register);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, id);
    }

    @RequestMapping(value = "/updateInfo", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.PUT)
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<Void> updateInfo(Long uid, @Valid @RequestBody Update update) {
        userService.updateInfo(MusixiseContext.getCurrentUid(), update);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/authByAccessToken/{platform}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse authenticateBySocialToken(@PathVariable String platform,
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
                        return new MusixiseResponse(ExceptionMsg.FAILED);
                    } else {
                        String jwt = userService.getTokenByLogin(login);
                        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, new JWTToken(jwt));
                    }
                } else {
                    return new MusixiseResponse<>(ExceptionMsg.FAILED, "create-connection-fail");
                }

            } catch (Exception e) {
                logger.error("Exception creating social user: ", e);
                return new MusixiseResponse<>(ExceptionMsg.FAILED);
            }
        } else {
            return new MusixiseResponse<>(ExceptionMsg.PARAM_ERROR);
        }

    }

    @RequestMapping(value = "/oauth/{platform}/callback", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @AppMethod
    @Override
    public MusixiseResponse<JWTToken> authenticateBySocialCode(@PathVariable String platform,
                                                     @RequestParam(value = "code", required = true) String code) {
        Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = socialConfiguration.getoAuth2ConnectionFactoryMap();
        if (!oAuth2ConnectionFactoryMap.containsKey(platform)) {
            return new MusixiseResponse<>(ExceptionMsg.PARAM_ERROR);
        }

        CustomOAuthService oAuthService = oAuthServices.getOAuthService(platform);
        Token accessToken = oAuthService.getAccessToken(null, new Verifier(code));

        SocialVO socialVO = oAuthService.getOAuthUser(accessToken);
        //检查是否已初始化
        String login = userManager.getLoginName(socialVO.getOpenId(), socialVO.getProvider());
        if (userService.isUserBindThis(socialVO.getOpenId(), socialVO.getProvider()) == null) {
            //需要初始化
            try {
                userManager.cleanAccount(login);
                login = userManager.createByOauth(socialVO);
            } catch (Exception e) {
                throw new MusixiseException("授权异常，请联系客服解决");
            }
        }

        String jwt = userService.getTokenByLogin(login);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }
}
