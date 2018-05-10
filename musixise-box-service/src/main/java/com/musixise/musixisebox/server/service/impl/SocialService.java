package com.musixise.musixisebox.server.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Service
public class SocialService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource UsersConnectionRepository usersConnectionRepository;

    public void createSocialConnection(String login, Connection<?> connection) {
        List<String> userConnectionRes = usersConnectionRepository.findUserIdsWithConnection(connection);

        if (userConnectionRes.size() == 0) {
            ConnectionRepository connectionRepository = usersConnectionRepository.createConnectionRepository(login);
            connectionRepository.addConnection(connection);
        }
    }
}
