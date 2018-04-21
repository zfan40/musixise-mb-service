package com.musixise.musixisebox.api.web.service;

import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.req.work.WorkMeta;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "作品接口", description = "作品接口", tags = "作品接口")
public interface WorkApi {

    @ApiOperation(value = "新建作品",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    MusixiseResponse<Long> create(Long uid, @Valid @RequestBody WorkMeta workMeta);

    @ApiOperation(value = "获取指定用户的作品列表",notes = "")
    MusixisePageResponse<List<WorkVO>> getListByUid(@PathVariable Long uid,
                                                           @RequestParam(value = "title", required = false) String title,
                                                           @RequestParam(value = "page", defaultValue = "1") int page,
                                                           @RequestParam(value = "size", defaultValue = "10") int size);


    @ApiOperation(value = "获取作品详细信息",notes = "")
    MusixiseResponse<WorkVO> getDetail(@PathVariable Long id);

    @ApiOperation(value = "更新作品详细信息",notes = "")
    @ApiImplicitParam(name = "uid", value = "用户ID", defaultValue = "", readOnly=true, dataType = "Long")
    MusixiseResponse<?> update(Long uid, @PathVariable Long id, @RequestBody WorkMeta workMeta);

}
