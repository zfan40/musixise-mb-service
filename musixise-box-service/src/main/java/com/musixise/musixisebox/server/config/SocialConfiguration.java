package com.musixise.musixisebox.server.config;

import com.musixise.musixisebox.server.repository.CustomSocialUsersConnectionRepository;
import com.musixise.musixisebox.server.repository.SocialUserConnectionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.ConnectionFactoryConfigurer;
import org.springframework.social.config.annotation.EnableSocial;
import org.springframework.social.config.annotation.SocialConfigurer;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.wechat.connect.WeChatConnectionFactory;
import org.springframework.social.weibo.connect.WeiboConnectionFactory;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Configuration
@EnableSocial
public class SocialConfiguration implements SocialConfigurer {

    @Resource SocialUserConnectionRepository socialUserConnectionRepository;

    private Map<String, OAuth2ConnectionFactory> oAuth2ConnectionFactoryMap = new HashMap<>();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void addConnectionFactories(ConnectionFactoryConfigurer connectionFactoryConfigurer, Environment environment) {

        //Weibo configuretion
        String weiboClientId = environment.getProperty("spring.social.weibo.clientId");
        String weiboClientSecret = environment.getProperty("spring.social.weibo.clientSecret");
        if (weiboClientId != null && weiboClientSecret != null) {
            logger.debug("Configuring WeiboConnectionFactory");


            ConnectionFactory weiBoConnectionFactory = new WeiboConnectionFactory(weiboClientId, weiboClientSecret);
            connectionFactoryConfigurer.addConnectionFactory( weiBoConnectionFactory );
            OAuth2ConnectionFactory weiboAuth2ConnectionFactory = new WeiboConnectionFactory(weiboClientId, weiboClientSecret);
            oAuth2ConnectionFactoryMap.put("weibo", weiboAuth2ConnectionFactory);
        }

        //wechat configuretion
        String wechatClientId = environment.getProperty("spring.social.wechat.clientId");
        String wechatClientSecret = environment.getProperty("spring.social.wechat.clientSecret");

        if (wechatClientId != null && wechatClientSecret != null) {
            logger.debug("Configuring WechatConnectionFactory");
            connectionFactoryConfigurer.addConnectionFactory(
                    new WeChatConnectionFactory(
                            wechatClientId,
                            wechatClientSecret
                    )
            );
            OAuth2ConnectionFactory weichatAuth2ConnectionFactory = new WeChatConnectionFactory(wechatClientId, wechatClientSecret);
            oAuth2ConnectionFactoryMap.put("wechat", weichatAuth2ConnectionFactory);
        }

    }

    @Override
    public UserIdSource getUserIdSource() {
        return new AuthenticationNameUserIdSource();
    }

    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
        return new CustomSocialUsersConnectionRepository(socialUserConnectionRepository, connectionFactoryLocator);
    }

    @Bean
    public SignInAdapter signInAdapter() {
        return null;
    }

    @Bean
    public ProviderSignInController providerSignInController(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository, SignInAdapter signInAdapter) throws Exception {
        ProviderSignInController providerSignInController = new ProviderSignInController(connectionFactoryLocator, usersConnectionRepository, signInAdapter);
        providerSignInController.setSignUpUrl("/social/signup");
        return providerSignInController;
    }

    @Bean
    public ProviderSignInUtils getProviderSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
        return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
    }

    public Map<String, OAuth2ConnectionFactory> getoAuth2ConnectionFactoryMap() {
        return oAuth2ConnectionFactoryMap;
    }
}
