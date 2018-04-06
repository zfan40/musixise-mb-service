package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.req.user.CreateWork;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixisePageResponse;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.repository.WorkRepository;
import com.musixise.musixisebox.service.FavoriteService;
import com.musixise.musixisebox.service.MusixiseService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.transfter.WorkTransfter;
import com.musixise.musixisebox.utils.CommonUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "用户接口", description = "作品接口")
@RequestMapping("/api/work")
public class WorkController {

    @Resource WorkRepository workRepository;

    @Resource MusixiseService musixiseService;

    @Resource UserService userService;

    @Resource FavoriteService favoriteService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "新建作品",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    @AppMethod(isLogin = true)
    public MusixiseResponse<Long> create(Long uid, @Valid @RequestBody CreateWork createWork) {

        Work work = WorkTransfter.getWork(createWork);
        work.setUserId(uid);
        workRepository.save(work);
        musixiseService.updateWorkCount(work.getId());
        return new MusixiseResponse(ExceptionMsg.SUCCESS);

    }


    @RequestMapping(value = "/getListByUid/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "获取指定用户的作品列表",notes = "")
    @AppMethod
    public MusixisePageResponse<List<WorkVO>> getListByUid(@PathVariable Long uid,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Work> workList = workRepository.findAllByUserIdOrderByIdDesc(uid, pageable);
        List<WorkVO> workVOList = new ArrayList<>();
        workList.forEach(work -> {
            UserVO userVO = userService.getById(work.getUserId());
            Boolean isFavorite = favoriteService.isFavorite(uid, work.getId());
            workVOList.add(WorkTransfter.getWorkVO(work, userVO, isFavorite));
        });

        return new MusixisePageResponse<>(workVOList, workList.getTotalElements(), size, page);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "获取作品详细信息",notes = "")
    @AppMethod
    public MusixiseResponse<WorkVO> getDetail(@PathVariable Long id) {
        Optional<Work> work = workRepository.findById(id);
        if (work.isPresent()) {
            WorkVO workVO = WorkTransfter.getWorkVO(work.get());
            return new MusixiseResponse(ExceptionMsg.SUCCESS, workVO);
        } else {
            return new MusixiseResponse(ExceptionMsg.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/updateWork/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "更新作品详细信息",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    @AppMethod(isLogin = true)
    public MusixiseResponse<Void> update(Long uid, @PathVariable Long id, @Valid @RequestBody CreateWork createWork) {
        Work one = workRepository.getOne(id);
        if (one.getUserId().equals(uid)) {
            CommonUtil.copyPropertiesIgnoreNull(createWork, one);
            workRepository.save(one);
            return new MusixiseResponse(ExceptionMsg.SUCCESS);
        } else {
            return new MusixiseResponse(ExceptionMsg.FAILED);
        }
    }
}
