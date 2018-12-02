package com.musixise.musixisebox.server.config;

import com.qiniu.common.Zone;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Configuration
public class QiucCloudConfiguration {
    private final Logger log = LoggerFactory.getLogger(QiucCloudConfiguration.class);

    @Value("${spring.qiniu.accessKey}") String ACCESS_KEY;
    @Value("${spring.qiniu.secretKey}") String SECRET_KEY;
    //创建上传对象

    @Bean
    public UploadManager uploadManager() {
        log.info("Configuring qiucloud provider");
        com.qiniu.storage.Configuration cfg = new com.qiniu.storage.Configuration(Zone.zone0());
        return  new UploadManager(cfg);
    }

    @Bean
    public Auth auth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }
}
