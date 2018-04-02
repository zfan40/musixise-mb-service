package com.musixise.musixisebox.controller;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.repository.MusixiserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api")
public class MusixiserController {

    @Resource MusixiserRepository musixiserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/musixisers", method = RequestMethod.GET)
    @AppMethod(isLogin = true)
    public ResponseData getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Musixiser> all = musixiserRepository.findAll(pageable);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", all.getTotalElements());
        jsonObject.put("data", all.getContent());

        return new ResponseData(ExceptionMsg.SUCCESS, jsonObject);

    }

}
