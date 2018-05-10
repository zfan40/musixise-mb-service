package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.MusixiseBoxApplication;
import com.musixise.musixisebox.server.domain.User;
import com.musixise.musixisebox.server.repository.UserRepository;
import com.musixise.musixisebox.server.service.UserService;
import com.musixise.musixisebox.rest.web.UserController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;
import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplication.class)
@WebAppConfiguration
public class UserControllerTest {

    @Test
    public void getInfo() throws Exception {

    }

    @Test
    public void register() throws Exception {

    }

    @Test
    public void updateInfo() throws Exception {

    }

    @Test
    public void authenticateBySocialToken() throws Exception {

    }

    @Test
    public void authenticateBySocialCode() throws Exception {

    }

    private MockMvc mvc;

    @Resource UserService userService;

    @Resource UserRepository userRepository;

    @Resource PasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
        UserController userController = new UserController();
        ReflectionTestUtils.setField(userController, "userService", userService);
        mvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void authorize() throws Exception {

        userRepository.deleteAll();
        User user = new User();
        user.setLogin("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setActivated(true);
        userRepository.save(user);
        //$2a$10$iLajzdp1vvA/Ujz44DoBAufg.SoZJvTQCLRMtmzZYOH6llc3UGsEG
        //$2a$10$2Lz8Ekdz4BDaE80vx6QtCu04qDNyhhVL9.xBVyssWifGz1a01s5m.
        mvc.perform(MockMvcRequestBuilders.post("/api/authenticate")
                .accept(MediaType.APPLICATION_JSON)
                .param("userName", "admin")
                .param("passWord", "admin")
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk());

        userRepository.delete(user);
    }


    class UserRepositorySpy {
        Optional<User> findOneByLogin(String login) {
            User user = new User();
            user.setId(1L);
            user.setActivated(true);
            user.setLogin("admin");
            user.setPassword("admin");
            return Optional.of(user);
        }
    }
}