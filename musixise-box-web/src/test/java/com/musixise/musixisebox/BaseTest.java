package com.musixise.musixisebox;

import com.musixise.musixisebox.server.security.jwt.TokenProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplicationTests.class)
@ActiveProfiles("test")
@PrepareForTest(RequestContextHolder.class) // Static.class contains static methods
public class BaseTest {

    @MockBean
    TokenProvider tokenProvider;

    private MockHttpServletRequest request;

    @Before
    public void setUpBase() throws Exception {

        Map<String,Object> claimsmap = new HashMap<String,Object>();//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claimsmap.put("uid", "1");
        Claims claims = new DefaultClaims(claimsmap);

        Mockito.when(tokenProvider.parseJWT("test")).thenReturn(claims);

        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.setParameter("access_token", "test");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request), true);

    }
}
