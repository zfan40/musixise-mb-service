package com.musixise.musixisebox.rest.web.shop;

import com.musixise.musixisebox.BaseTest;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.server.utils.DateUtil;
import com.musixise.musixisebox.shop.domain.Order;
import com.musixise.musixisebox.shop.enums.OrderEnum;
import com.musixise.musixisebox.shop.repository.OrderRepository;
import com.musixise.musixisebox.shop.rest.web.PayController;
import com.musixise.musixisebox.shop.rest.web.vo.req.pay.UnifiedorderVO;
import com.musixise.musixisebox.shop.rest.web.vo.resp.pay.WCPayRequestVO;
import com.musixise.musixisebox.shop.utils.OrderUtil;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.stream.Collectors;

public class PayControllerTest extends BaseTest {

    @Autowired
    private PayController payController;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void unifiedorder() {
        //2018-12-16 19:44:05
        Date date = DateUtil.asDate("2018-12-16 19:44:05");

        Long orderIdLong = 99L;
        String orderId = OrderUtil.INSTANCE.genOrderId(1L, orderIdLong, date);

        //order before
        Order order = orderRepository.getOne(orderIdLong);
        Assert.assertNotNull(orderId);
        Assert.assertEquals(java.util.Optional.of(Long.valueOf(0L)).get().longValue(), order.getStatus());

        //order after check
        UnifiedorderVO unifiedorderVO = new UnifiedorderVO();
        unifiedorderVO.setOrderId(orderId);
        MusixiseResponse<WCPayRequestVO> unifiedorder = payController.unifiedorder(unifiedorderVO);
        Assert.assertNotNull(unifiedorder);
        Assert.assertEquals("wx353a60a8b049d366", unifiedorder.getData().getAppId());
        Assert.assertTrue(unifiedorder.getData().getPackageStr().indexOf("prepay_id") != -1);
        Assert.assertEquals("MD5", unifiedorder.getData().getSignType());

        checkStatus(orderIdLong, OrderEnum.PEDDING_PAY);


    }

    @Test
    public void payNotify() {

        Date date = DateUtil.asDate("2018-12-16 19:44:05");

        Long orderIdLong = 100L;
        String orderId = OrderUtil.INSTANCE.genOrderId(1L, orderIdLong, date);

        System.out.println(orderId);

        InputStream resourceAsStream = getClass()
                .getClassLoader()
                .getResourceAsStream("data/wechat-pay-notify");

        String result = new BufferedReader(new InputStreamReader(resourceAsStream))
                .lines().collect(Collectors.joining(System.lineSeparator()));

        String payNotify = payController.payNotify(result);
        Assert.assertNotNull(payNotify);
        Assert.assertTrue(payNotify.indexOf("OK") != -1);

        checkStatus(orderIdLong, OrderEnum.WAIT_DELIVER_GOODS);
    }

}
