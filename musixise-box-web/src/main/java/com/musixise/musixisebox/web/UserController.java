package com.musixise.musixisebox.web;

import com.google.common.base.Preconditions;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.aop.MusixiseContext;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.exception.MusixiseException;
import com.musixise.musixisebox.api.web.vo.req.user.Login;
import com.musixise.musixisebox.api.web.vo.req.user.Register;
import com.musixise.musixisebox.api.web.vo.req.user.Update;
import com.musixise.musixisebox.api.web.vo.resp.JWTToken;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.config.SocialConfiguration;
import com.musixise.musixisebox.domain.User;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.manager.UserManager;
import com.musixise.musixisebox.repository.UserRepository;
import com.musixise.musixisebox.service.CustomOAuthService;
import com.musixise.musixisebox.service.FollowService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.service.impl.OAuthServices;
import com.musixise.musixisebox.service.impl.SocialService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "用户接口", description = "用户接口描述", tags = "用户常用接口")
public class UserController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UserService userService;

    @Resource SocialService socialService;

    @Resource OAuthServices oAuthServices;

    @Resource SocialConfiguration socialConfiguration;

    @Resource UserManager userManager;

    @Resource UserRepository userRepository;

    @Resource FollowService followService;

    @RequestMapping(value = "/authenticate", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "授权接口",notes = "获取 accesstoken")
    @AppMethod
    public MusixiseResponse<JWTToken> authorize(Model model, @Valid @RequestBody Login login) {

        Preconditions.checkArgument(login.getUserName() != null && login.getPassWord() != null, "请输入用户名和密码");
        String jwt = userService.auth(login);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }

    @RequestMapping(value = "/detail/{uid}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.GET)
    @ApiOperation(value = "获取用户详细信息", notes = "通过传入用户ID ")
    @AppMethod
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
    @ApiOperation(value = "获取当前用户信息")
    @AppMethod(isLogin = true)
    public MusixiseResponse<UserVO> getCuurentUserInfo(Long uid) {
        UserVO userVO = Optional.ofNullable(userService.getById(uid)).orElseThrow(() -> new MusixiseException("不存在的用户"));
        return new MusixiseResponse<>(userVO);
    }

    @RequestMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @ApiOperation(value = "注册一个账号", notes = "注册成功，返回用户ID")
    @AppMethod
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
    @ApiOperation(value = "更新用户信息", notes = "需要登录")
    @AppMethod(isLogin = true)
    public MusixiseResponse<Void> updateInfo(Long uid, @Valid @RequestBody Update update) {
        userService.updateInfo(uid, update);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/authByAccessToken/{platform}", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
    @AppMethod
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
    @ApiOperation(value = "社交账号 OAUTH ", notes = "")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "platform", value = "社交平台 wechat, weibo",  required = true, dataTypeClass = String.class),
            @ApiImplicitParam(name = "code", value = "code", required = true, dataTypeClass = String.class)
    })
    @AppMethod
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
                login = userManager.createByOauth(socialVO);
            } catch (Exception e) {
                throw new MusixiseException("授权异常，请联系客服解决");
            }
        }

        String jwt = userService.getTokenByLogin(login);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, new JWTToken(jwt));
    }
}
