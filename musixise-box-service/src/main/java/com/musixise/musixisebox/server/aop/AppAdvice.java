package com.musixise.musixisebox.server.aop;

import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.server.service.UserService;
import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;

/**
 * Created by zhaowei on 2018/4/1.
 */
@Aspect
@Component
public class AppAdvice implements Ordered {

    private final static String AUTHORIZATION_HEADER = "Authorization";

    public final static String AUTHORIZATION_TOKEN = "access_token";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public int getOrder() {
        return 1;
    }

    @Resource UserService userService;

    @Pointcut("@annotation(com.musixise.musixisebox.server.aop.AppMethod)")
    public void loginMethodPointcut() {}

    @Around("loginMethodPointcut() && @annotation(appMethod)")
    public Object Interception(ProceedingJoinPoint point, AppMethod appMethod) throws Throwable {

        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        HttpServletRequest request = sra.getRequest();

        Object result = null;
        //需要检测登录
        String accessToken = request.getParameter(AUTHORIZATION_TOKEN);
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (!StringUtils.hasText(accessToken) && StringUtils.hasText(bearerToken)) {
            accessToken = bearerToken;
        }

        if (StringUtils.hasText(accessToken)) {
            //获取登录状态
            Boolean islogin = islogin(accessToken);

            if (appMethod.isLogin()) {
                //check
                if (!islogin) {
                    return  new MusixiseResponse(ExceptionMsg.NEED_LOGIN);
                } else {
                    Object[] args = point.getArgs();
                    args[0] = userService.getUserIdByToken(accessToken);;
                    return point.proceed(args);
                }
            }
        } else {
            if (appMethod.isLogin()) {
                return  new MusixiseResponse(ExceptionMsg.NEED_LOGIN);
            } else {
                MusixiseContext.set("_uid", 0L);
            }
        }

        if (result == null) {
            result = point.proceed();
        }

        return result;
    }

    private void injectUid(Long uid) {

    }

    private Boolean islogin(String accessToken) {
        //检测 access_token 是否存在
        Long userId = userService.getUserIdByToken(accessToken);
        //写入用户ID
        MusixiseContext.set("_uid", userId);
        return userId > 0;
    }

    @Before("within(com.musixise.musixisebox..*)")
    public void addBeforeLogger(JoinPoint joinPoint) {
        logger.info("run before " + getInvokeName(joinPoint));
        logger.info(joinPoint.getSignature().toString());
        logger.info(parseParames(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "within(com.musixise.musixisebox..*)", returning = "rvt")
    public void addAfterReturningLogger(JoinPoint joinPoint, Object rvt) {
        StringBuffer param = new StringBuffer("get params[{}] ");
        String retString = "";
        if (rvt != null) {
            retString = ReflectionToStringBuilder.toString(rvt);
        } else {
            retString = "null";
        }
        logger.info("run after " + getInvokeName(joinPoint)+ " returnObj="+retString.toString());
    }

    @AfterThrowing(pointcut = "within(com.musixise.musixisebox..*) && @annotation(appMethod)", throwing = "ex")
    public void addAfterThrowingLogger(JoinPoint joinPoint, AppMethod appMethod, Exception ex) {
        logger.error("run exception " + getInvokeName(joinPoint) + " Params "+ ReflectionToStringBuilder.toString(joinPoint.getArgs()) + "  EXCEPTION", ex);
    }

    private String parseParames(Object[] parames) {
        if (null == parames || parames.length <= 0) {
            return "";
        }
        StringBuffer param = new StringBuffer("get params[{}] ");
        for (Object obj : parames) {
            param.append(ReflectionToStringBuilder.toString(obj)).append("  ");
        }
        return param.toString();
    }


    private String getInvokeName(JoinPoint joinPoint) {
        //正在被通知的方法相关信息
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取被拦截的方法
        Method method = signature.getMethod();
        //获取被拦截的方法名
        String methodName = method.getName();

        return String.format("%s.%s",signature.getDeclaringTypeName(), methodName);
    }

}
