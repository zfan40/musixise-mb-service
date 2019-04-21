package com.musixise.musixisebox.rest.manager;

import com.musixise.musixisebox.BaseTest;
import com.musixise.musixisebox.shop.manager.PurchaseListManager;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class PurchaseListManagerTest extends BaseTest {

    @Autowired
    private PurchaseListManager purchaseListManager;

    @Test
    public void create() {
        Long userId = 1L;
        Long pid = 1L;
        Long wid = 1L;
        Long orderId = 1L;
        purchaseListManager.create(userId, pid, wid, orderId);

        boolean exists = purchaseListManager.exists(userId, pid, wid);
        Assert.assertTrue(exists);
    }
}
