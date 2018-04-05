package com.musixise.musixisebox.config;

import com.musixise.musixisebox.MusixiseException;
import com.musixise.musixisebox.domain.result.ResponseData;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by zhaowei on 2018/4/5.
 */
@ControllerAdvice
public class MuExceptionHandler implements Ordered {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public ResponseData defaultErrorHandler(Exception e, HttpServletRequest request) throws Exception {
        logger.info("请求地址：" + request.getRequestURL());
        return ResponseData.errorResponse(e.getMessage());
    }

    @Override
    public int getOrder() {
        return 3;
    }

    @ExceptionHandler(MusixiseException.class)
    @ResponseBody
    ResponseData handleBusinessException(MusixiseException e, HttpServletRequest request){
        logger.info("请求地址：" + request.getRequestURL());
        return ResponseData.errorResponse(e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    ResponseData handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request){

        logger.info("请求地址：" + request.getRequestURL());
        return ResponseData.errorResponse(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }
}
