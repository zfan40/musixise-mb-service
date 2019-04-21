package com.musixise.musixisebox.rest.web.shop;

import com.musixise.musixisebox.BaseTest;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.shop.enums.OrderEnum;
import com.musixise.musixisebox.shop.rest.web.MyOrderController;
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderListQueryVO;
import com.musixise.musixisebox.shop.rest.web.vo.req.OrderVO;
import com.musixise.musixisebox.shop.rest.web.vo.req.PayVO;
import com.musixise.musixisebox.shop.rest.web.vo.resp.MyOrderVO;
import com.musixise.musixisebox.shop.utils.OrderUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MyOrderControllerTest extends BaseTest {

    @Autowired
    private MyOrderController myOrderController;


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void createMusixBox() {

        OrderVO orderVO = new OrderVO(1L, 1L, 1, 1, "meesagetest");
        MusixiseResponse<String> stringMusixiseResponse = myOrderController.create(orderVO);
        Assert.assertNotNull(stringMusixiseResponse);
        //Assert.assertEquals("201904202300000000010000000065", stringMusixiseResponse.getData());
        checkStatus(OrderUtil.INSTANCE.getOrderId(stringMusixiseResponse.getData()), OrderEnum.UNPAY);
        Assert.assertEquals("0", stringMusixiseResponse.getRspCode());

    }

    @Test
    public void createMusixDownload() {
        OrderVO orderVO = new OrderVO(1L, 1L, 1, 1, "meesagetest");
        MusixiseResponse<String> stringMusixiseResponse = myOrderController.create(orderVO);
        Assert.assertNotNull(stringMusixiseResponse);
    }

    @Test
    public void pay() {
        PayVO payVO = new PayVO(99L);
        MusixiseResponse<Boolean> pay = myOrderController.pay(payVO);
        Assert.assertNotNull(pay);
        Assert.assertTrue(pay.getData());
    }

    @Test
    public void myOrderList() throws Exception {

        OrderListQueryVO orderListQueryVO = new OrderListQueryVO();
        MusixisePageResponse<List<MyOrderVO>> listMusixisePageResponse = myOrderController.myOrderList(orderListQueryVO);
        Assert.assertNotNull(listMusixisePageResponse);
        Assert.assertTrue(listMusixisePageResponse.getData().getTotal() > 1);

        List<MyOrderVO> list = (List) listMusixisePageResponse.getData().getList();

        Assert.assertTrue(list.size() > 1);
    }

}
