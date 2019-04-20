package com.musixise.musixisebox.rest.web.shop;

import com.musixise.musixisebox.MusixiseBoxApplicationTests;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.server.security.jwt.TokenProvider;
import com.musixise.musixisebox.shop.rest.web.MyOrderController;
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderListQueryVO;
import com.musixise.musixisebox.shop.rest.web.vo.resp.MyOrderVO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.impl.DefaultClaims;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = MusixiseBoxApplicationTests.class)
@ActiveProfiles("test")
@PrepareForTest(RequestContextHolder.class) // Static.class contains static methods
public class MyOrderControllerTest {

    @Autowired
    private MyOrderController myOrderController;

    @MockBean
    TokenProvider tokenProvider;

    private MockHttpServletRequest request;


    @Before
    public void setUp() throws Exception {

        Map<String,Object> claimsmap = new HashMap<String,Object>();//创建payload的私有声明（根据特定的业务需要添加，如果要拿这个做验证，一般是需要和jwt的接收方提前沟通好验证方式的）
        claimsmap.put("uid", "1");
        Claims claims = new DefaultClaims(claimsmap);

        Mockito.when(tokenProvider.parseJWT("test")).thenReturn(claims);

        request = new MockHttpServletRequest();
        request.setCharacterEncoding("UTF-8");
        request.setParameter("access_token", "test");
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request), true);

    }

    @Test
    public void myOrderList() throws Exception {

        OrderListQueryVO orderListQueryVO = new OrderListQueryVO();
        MusixisePageResponse<List<MyOrderVO>> listMusixisePageResponse = myOrderController.myOrderList(orderListQueryVO);
        Assert.assertNotNull(listMusixisePageResponse);
        Assert.assertEquals(1, listMusixisePageResponse.getData().getTotal());

        List<MyOrderVO> list = (List) listMusixisePageResponse.getData().getList();
        Assert.assertEquals("201812161100000000010000000001", list.get(0).getOrderId());
        Assert.assertEquals("http://oiqvdjk3s.bkt.clouddn.com/kuNziglJ_test.txt", list.get(0).getContent().getUrl());
    }

}
