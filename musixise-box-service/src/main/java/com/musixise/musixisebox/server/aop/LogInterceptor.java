package com.musixise.musixisebox.server.aop;

import com.musixise.musixisebox.server.utils.IpUtil;
import org.slf4j.MDC;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class LogInterceptor extends HandlerInterceptorAdapter {

    private final static String TRACK_ID = "trackId";
    private final static String TRACK_CLIENT_IP = "trackClientIp";

    @Override
    public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3)
            throws Exception {

        MDC.remove(TRACK_ID);
    }

    @Override
    public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1,
                           Object arg2, ModelAndView arg3) throws Exception {
    }

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response, Object handler) throws Exception {

        String token = UUID.randomUUID().toString().replace("-", "");
        MDC.put(TRACK_ID, token);

        MusixiseContext.set("remoteip", IpUtil.getIpAddr(request));
        MDC.put(TRACK_CLIENT_IP, MusixiseContext.getRemoteIp());

        return true;
    }
}
