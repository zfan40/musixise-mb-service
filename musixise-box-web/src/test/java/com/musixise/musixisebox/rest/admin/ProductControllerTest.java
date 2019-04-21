package com.musixise.musixisebox.rest.admin;

import com.musixise.musixisebox.BaseTest;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.shop.domain.Product;
import com.musixise.musixisebox.shop.enums.ProductTypeEnum;
import com.musixise.musixisebox.shop.repository.ProductRepository;
import com.musixise.musixisebox.shop.rest.admin.ProductController;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

public class ProductControllerTest extends BaseTest {

    @Autowired
    private ProductController productController;

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void createMusixBox() {
        BigDecimal bigDecimal = new BigDecimal(10.01);
        Product product = new Product(0,
                ProductTypeEnum.MUSIX_BOX.getType(),
                "音乐盒",
                "intro",
                bigDecimal,
                "previewPic",
                "previewVideo",
                0

        );
        MusixiseResponse<Long> response = productController.create(product);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getData() > 0);

        Product checkProduct = productRepository.getOne(product.getId());
        Assert.assertNotNull(checkProduct);
        Assert.assertEquals("intro", checkProduct.getIntro());
        Assert.assertEquals("音乐盒", checkProduct.getName());
        Assert.assertEquals(1, ProductTypeEnum.MUSIX_BOX.getType());
        Assert.assertEquals("10.01", checkProduct.getPrice().toString());
        Assert.assertEquals("previewPic", checkProduct.getPreviewPic());
        Assert.assertEquals("previewVideo", checkProduct.getPreviewVideo());

    }

    @Test
    public void createMusixDownload() {
        BigDecimal bigDecimal = new BigDecimal(6.01);
        Product product = new Product(0,
                ProductTypeEnum.MUSIX_DOWNLOAD.getType(),
                "音频下载",
                "intro",
                bigDecimal,
                "previewPic",
                "previewVideo",
                0

        );
        MusixiseResponse<Long> response = productController.create(product);
        Assert.assertNotNull(response);
        Assert.assertTrue(response.getData() > 0);

        Product checkProduct = productRepository.getOne(product.getId());
        Assert.assertNotNull(checkProduct);
        Assert.assertEquals("intro", checkProduct.getIntro());
        Assert.assertEquals("音频下载", checkProduct.getName());
        Assert.assertEquals(100, ProductTypeEnum.MUSIX_DOWNLOAD.getType());
        Assert.assertEquals("6.01", checkProduct.getPrice().toString());
        Assert.assertEquals("previewPic", checkProduct.getPreviewPic());
        Assert.assertEquals("previewVideo", checkProduct.getPreviewVideo());

    }
}
