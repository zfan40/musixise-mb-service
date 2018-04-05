package com.musixise.musixisebox.controller;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.req.user.CreateWork;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.repository.WorkRepository;
import com.musixise.musixisebox.service.MusixiseService;
import com.musixise.musixisebox.transfter.WorkTransfter;
import com.musixise.musixisebox.utils.CommonUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/5.
 */

@RestController
@RequestMapping("/api/work")
public class WorkController {

    @Resource WorkRepository workRepository;

    @Resource MusixiseService musixiseService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    public ResponseData create(Long uid, @Valid CreateWork createWork) {

        Work word = WorkTransfter.getWord(createWork);
        word.setUserId(uid);
        workRepository.save(word);
        musixiseService.updateWorkCount(uid);
        return new ResponseData(ExceptionMsg.SUCCESS);

    }


    @RequestMapping(value = "/getListByUid/{uid}", method = RequestMethod.GET)
    @AppMethod
    public ResponseData getListByUid(@PathVariable Long uid,
                                     @RequestParam(value = "page", defaultValue = "!") int page,
                                     @RequestParam(value = "size", defaultValue = "10") int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);
        Page<Work> workList = workRepository.findAllByUserIdOrderByIdDesc(uid, pageable);
        List<WorkVO> workVOList = new ArrayList<>();
        workList.forEach(work -> {
            workVOList.add(WorkTransfter.getWorkVO(work));
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", workList.getTotalElements());
        jsonObject.put("list", workVOList);
        jsonObject.put("size", size);
        jsonObject.put("current", page);

        return new ResponseData(ExceptionMsg.SUCCESS, jsonObject);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @AppMethod
    public ResponseData getDetail(@PathVariable Long id) {
        Optional<Work> work = workRepository.findById(id);
        if (work.isPresent()) {
            WorkVO workVO = WorkTransfter.getWorkVO(work.get());
            return new ResponseData(ExceptionMsg.SUCCESS, workVO);
        } else {
            return new ResponseData(ExceptionMsg.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/updateWork/{id}", method = RequestMethod.PUT)
    @AppMethod(isLogin = true)
    public ResponseData update(Long uid, @PathVariable Long id, @Valid CreateWork createWork) {
        Work one = workRepository.getOne(id);
        if (one.getUserId().equals(uid)) {
            CommonUtil.copyPropertiesIgnoreNull(createWork, one);
            workRepository.save(one);
            return new ResponseData(ExceptionMsg.SUCCESS);
        } else {
            return new ResponseData(ExceptionMsg.FAILED);
        }
    }
}
