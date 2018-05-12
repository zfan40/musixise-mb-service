package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.MusixiseBoxApplication;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.rest.admin.MusixiserAdminController;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
/**
 * Created by zhaowei on 2018/4/2.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplication.class)

public class MusixiserControllerTest {

    private MockMvc mvc;

    @Resource MusixiserRepository musixiserRepository;

    @Before
    public void setUp() {
        MusixiserAdminController musixiserController = new MusixiserAdminController();
        ReflectionTestUtils.setField(musixiserController, "musixiserRepository", musixiserRepository);
        mvc = MockMvcBuilders.standaloneSetup(musixiserController).build();
    }

    @Test
    public void getMusixisers() throws Exception {

        Musixiser musixiser = new Musixiser();
        musixiser.setUserId(1L);
        musixiser.setRealname("name");
        musixiserRepository.save(musixiser);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("/api/musixisers").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();

        musixiserRepository.deleteAll();


    }

}