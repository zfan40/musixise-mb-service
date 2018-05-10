package com.musixise.musixisebox.server.service.impl;

import com.musixise.musixisebox.server.service.CustomOAuthService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class OAuthServices {

    @Resource
    List<CustomOAuthService> oAuthServiceDeractors;

    public CustomOAuthService getOAuthService(String type){
        Optional<CustomOAuthService> oAuthService = oAuthServiceDeractors.stream().filter(o -> o.getoAuthType().equals(type))
                .findFirst();
        if(oAuthService.isPresent()){
            return oAuthService.get();
        }
        return null;
    }

    public List<CustomOAuthService> getAllOAuthServices(){
        return oAuthServiceDeractors;
    }

}
