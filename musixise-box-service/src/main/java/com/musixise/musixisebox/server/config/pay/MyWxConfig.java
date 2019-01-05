package com.musixise.musixisebox.server.config.pay;


import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

@Configuration
public class MyWxConfig extends WXPayConfig {

    @Value("${spring.wechat.pay.appId}") String appId;
    @Value("${spring.wechat.pay.mchId}") String mchId;
    @Value("${spring.wechat.pay.key}") String key;
    @Value("${spring.wechat.pay.payNotifyUrl}") String payNotifyUrl;

    private byte[] certData;

    public MyWxConfig() throws Exception {

        InputStream certStream = getClass()
                .getClassLoader()
                .getResourceAsStream("apiclient_cert.p12");

        this.certData = new byte[(int) certStream.available()];
        certStream.read(this.certData);
        certStream.close();
    }

    @Override
    public String getAppID() {
        return appId;
    }

    @Override
    public String getMchID() {
        return mchId;
    }

    @Override
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public InputStream getCertStream() {
        ByteArrayInputStream certBis = new ByteArrayInputStream(this.certData);
        return certBis;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return WXPayDomainSimpleImpl.instance();
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    public String getPayNotifyUrl() {
        return payNotifyUrl;
    }

    public void setPayNotifyUrl(String payNotifyUrl) {
        this.payNotifyUrl = payNotifyUrl;
    }
}

