package com.musixise.musixisebox.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaowei on 2018/4/1.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface AppMethod {
    /**
     * 是否已登录
     * @return
     */
    boolean isLogin() default false;
}
