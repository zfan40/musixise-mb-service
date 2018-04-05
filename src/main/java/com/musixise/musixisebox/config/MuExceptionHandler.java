package com.musixise.musixisebox.config;

import com.musixise.musixisebox.MusixiseException;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * Created by zhaowei on 2018/4/5.
 */
@ControllerAdvice
public class MuExceptionHandler implements Ordered {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 处理系统级异常
     * @param e
     * @param request
     * @return
     * @throws Exception
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public MusixiseResponse defaultErrorHandler(Exception e, HttpServletRequest request) throws Exception {
        logger.info("请求地址：" + request.getRequestURL());
        return MusixiseResponse.errorResponse(e.getMessage());
    }

    @Override
    public int getOrder() {
        return 3;
    }

    /**
     * 处理应用程序异常
     * @param e
     * @param request
     * @return
     */
    @ExceptionHandler(MusixiseException.class)
    @ResponseBody
    MusixiseResponse handleBusinessException(MusixiseException e, HttpServletRequest request){
        logger.info("请求地址：" + request.getRequestURL());
        return MusixiseResponse.errorResponse(e.getMessage());
    }

    /**
     * 处理所有接口数据验证异常
     * @param e MethodArgumentTypeMismatchException
     * @return
     */
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    @ResponseBody
    MusixiseResponse handleMethodArgumentNotValidException(MethodArgumentTypeMismatchException e,
                                                           HttpServletRequest request){

        logger.info("请求地址：" + request.getRequestURL());
        return new MusixiseResponse(ExceptionMsg.PARAM_ERROR, e.getMessage());
    }

    @ExceptionHandler(BindException.class)
    @ResponseBody
    public MusixiseResponse processValidationError(BindException ex) {
        logger.error(ex.getMessage(), ex);
        String result = ex
                .getBindingResult()
                .getAllErrors()
                .stream()
                .map(ObjectError::getDefaultMessage)
                .collect(Collectors.joining(","));

        return new MusixiseResponse(ExceptionMsg.PARAM_ERROR, result);
    }
}
