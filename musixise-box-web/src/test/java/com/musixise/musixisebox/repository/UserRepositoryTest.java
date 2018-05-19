package com.musixise.musixisebox.repository;

import com.alibaba.fastjson.JSON;
import com.musixise.musixisebox.MusixiseBoxApplication;
import com.musixise.musixisebox.server.domain.User;
import com.musixise.musixisebox.server.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/5/19.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplication.class)
@WebAppConfiguration
public class UserRepositoryTest {

    @Resource UserRepository userRepository;


    @Resource PasswordEncoder passwordEncoder;

    @Test
    public void findOneByLogin() throws Exception {

    }


    @Test
    public void save() {

        User user = new User();
        user.setLogin("test");
        user.setEmail("test12234@musixise.com");
        user.setPassword(passwordEncoder.encode("aa"));
        user.setActivated(true);
        User save = userRepository.save(user);
        System.out.println(JSON.toJSONString(save));

    }
}