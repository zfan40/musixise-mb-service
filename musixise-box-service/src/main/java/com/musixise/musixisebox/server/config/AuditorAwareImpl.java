package com.musixise.musixisebox.server.config;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/6.
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("admin");
    }


}

