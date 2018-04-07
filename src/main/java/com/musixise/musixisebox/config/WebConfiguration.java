package com.musixise.musixisebox.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;
import org.springframework.data.web.config.SortHandlerMethodArgumentResolverCustomizer;

/**
 * Created by zhaowei on 2018/4/7.
 */
@Configuration
public class WebConfiguration {

    @Bean SortHandlerMethodArgumentResolverCustomizer sortCustomizer() {
        return s -> s.setPropertyDelimiter("<-->");
    }

    @Bean
    PageableHandlerMethodArgumentResolverCustomizer pageCutomeizer() {
        return s-> {
            s.setOneIndexedParameters(true);
        };
    }

}
