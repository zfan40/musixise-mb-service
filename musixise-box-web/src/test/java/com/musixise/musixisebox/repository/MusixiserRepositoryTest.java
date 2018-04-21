package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.MusixiseBoxApplication;
import com.musixise.musixisebox.domain.Musixiser;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplication.class)
@WebAppConfiguration
public class MusixiserRepositoryTest {

    @Autowired
    private MusixiserRepository musixiserRepository;

    @Test
    public void test() {

    }

    @Test
    public void findOneByUserId() throws Exception {

        musixiserRepository.deleteAll();
        Musixiser musixiser = new Musixiser();
        musixiser.setUserId(1L);
        musixiser.setRealname("musixiser");
        musixiser.setBrief("brief");
        musixiserRepository.save(musixiser);

        Long userId = 1L;
        Optional<Musixiser> oneByUserId = musixiserRepository.findOneByUserId(userId);
        Assert.assertEquals("musixiser", oneByUserId.get().getRealname());
        musixiserRepository.delete(oneByUserId.get());
    }

}