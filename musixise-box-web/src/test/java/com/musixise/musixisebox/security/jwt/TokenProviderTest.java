package com.musixise.musixisebox.security.jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RunWith(SpringRunner.class)
public class TokenProviderTest {

    @Test
    public void test() throws Exception {
        TokenProvider tokenProvider = new TokenProvider();
        String jwt = tokenProvider.createToken("jwt", 1L, 60000L);

        System.out.println(jwt);
        Claims claims = tokenProvider.parseJWT(jwt);
        System.out.println(JSON.toJSONString(claims));
    }

}