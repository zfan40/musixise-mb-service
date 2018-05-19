package com.musixise.musixisebox.manager;

import com.musixise.musixisebox.MusixiseBoxApplication;
import com.musixise.musixisebox.api.web.vo.resp.SocialVO;
import com.musixise.musixisebox.server.manager.UserManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/5/16.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplication.class)
@WebAppConfiguration
public class UserManagerTest {

    @Resource UserManager userManager;

    //0[openId=ocAsI1G4tMzraMwzlG73T62OTSyg,nickName=c.🦄,avatar=http://thirdwx.qlogo.cn/mmopen/vi_32/kzVSeCJnzCKXZXJs8fvichHPVk5dW37UPj9cCYEl7Sg7pwAu3LbIlAh6wBBvzKnDv8FELibM6MxFxDBvalDNZ2OA/132,sex=2,provider=wechat,accessToken=9_rr8dugIDVl3FWp6hhvhdOHcBLoj94rhleJGoAdhjeu7hF8H_72qcBcfUU6KYuBnmi_D4yruL4IabJjCf9J1ZliDhvfusULAuts4GX-mYzW4,refreshToken=9_Pi6XiHl-iodsUJ28Qt0CenK0MYmN0Rl6bmxUMryOoFodsNrUTKXJMOAWL6aMLmTUxl9hx4F49wjttXEGbZWYMEDYSXliLg4WKU05WeREMRw,expiresIn=1776793]


    @Test
    public void createByOauth() throws Exception {

        SocialVO socialVO = new SocialVO();
        socialVO.setAccessToken("aaa");
        socialVO.setAvatar("");
        socialVO.setOpenId("124demo");
        socialVO.setProvider("wechat");
        socialVO.setNickName("=c.\uD83E\uDD84,中文aa");

        String loginName = userManager.getLoginName(socialVO.getOpenId(), socialVO.getProvider());
        userManager.cleanAccount(loginName);
        String byOauth = userManager.createByOauth(socialVO);
        System.out.println(byOauth);

    }

}