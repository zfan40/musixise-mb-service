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
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@RequestMapping("/api")
public class MusixiserController {

    @Resource MusixiserRepository musixiserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/musixisers", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    public ResponseData getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Musixiser> all = musixiserRepository.findAll(pageable);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", all.getTotalElements());
        jsonObject.put("list", all.getContent());
        jsonObject.put("size", size);
        jsonObject.put("current", page);

        return new ResponseData(ExceptionMsg.SUCCESS, jsonObject);

    }

    @RequestMapping(value = "/musixisers", method = RequestMethod.PUT)
    @AppMethod(isAdmin = true)
    public ResponseData updateMusixser(@Valid Musixiser musixiser) {
        musixiserRepository.save(musixiser);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.GET)
    @AppMethod(isAdmin = true)
    public ResponseData getMusixiser(@PathVariable Long id) {
        Musixiser musixiser = musixiserRepository.getOne(id);
        return new ResponseData(ExceptionMsg.SUCCESS, musixiser);
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.DELETE)
    @AppMethod(isAdmin = true)
    public ResponseData delMusixiser(@PathVariable Long id) {
        musixiserRepository.deleteById(id);
        return new ResponseData(ExceptionMsg.SUCCESS);
    }


}
