package com.musixise.musixisebox.web;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.aop.MusixiseContext;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.service.WorkApi;
import com.musixise.musixisebox.api.web.vo.req.work.WorkMeta;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.manager.WorkManager;
import com.musixise.musixisebox.repository.WorkRepository;
import com.musixise.musixisebox.service.MusixiseService;
import com.musixise.musixisebox.transfter.WorkTransfter;
import com.musixise.musixisebox.utils.CommonUtil;
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

@RequestMapping("/api/v1/work")
public class WorkController implements WorkApi {

    @Resource WorkRepository workRepository;

    @Resource MusixiseService musixiseService;

    @Resource WorkManager workManager;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "新建作品",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<Long> create(Long uid, @Valid @RequestBody WorkMeta workMeta) {

        Work work = WorkTransfter.getWork(workMeta);
        work.setUserId(uid);
        workRepository.save(work);
        musixiseService.updateWorkCount(work.getId());
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, work.getId());
    }

    @RequestMapping(value = "/getListByUid/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "获取指定用户的作品列表",notes = "")
    @AppMethod
    @Override
    public MusixisePageResponse<List<WorkVO>> getListByUid(@PathVariable Long uid,
                                                           @RequestParam(value = "title", required = false) String title,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);

        Page<Work> workList = null;
        if (title == null) {
            workList = workRepository.findAllByUserIdOrderByIdDesc(uid, pageable);
        } else {
            workList = workRepository.findAllByUserIdAndTitleLikeOrderByIdDesc(uid, "%"+title+"%", pageable);

        }
        List<WorkVO> workVOList = new ArrayList<>();
        workList.forEach(work -> {
            WorkVO workVO = workManager.getWorkVO(MusixiseContext.getCurrentUid(), work);
            workVOList.add(workVO);
        });

        return new MusixisePageResponse<>(workVOList, workList.getTotalElements(), size, page);
    }

    @RequestMapping(value = "/detail/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "获取作品详细信息",notes = "")
    @AppMethod
    @Override
    public MusixiseResponse<WorkVO> getDetail(@PathVariable Long id) {
        Optional<Work> work = workRepository.findById(id);
        return work.map(w -> {
            WorkVO workVO = workManager.getWorkVO(MusixiseContext.getCurrentUid(), w);
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS, workVO);
        }).orElse( new MusixiseResponse<>(ExceptionMsg.NOT_EXIST));
    }

    @RequestMapping(value = "/updateWork/{id}", method = RequestMethod.PUT)
    @ApiOperation(value = "更新作品详细信息",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    @AppMethod(isLogin = true)
    @Override
    public MusixiseResponse<?> update(Long uid, @PathVariable Long id, @RequestBody WorkMeta workMeta) {
        return workRepository.findById(id).map(work -> {
            CommonUtil.copyPropertiesIgnoreNull(workMeta, work);
            workRepository.save(work);
            return new MusixiseResponse<>(ExceptionMsg.SUCCESS);
        }).orElse(new MusixiseResponse<>(ExceptionMsg.FAILED));
    }
}
