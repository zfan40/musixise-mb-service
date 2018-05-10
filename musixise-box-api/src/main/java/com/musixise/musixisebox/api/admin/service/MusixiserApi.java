package com.musixise.musixisebox.api.admin.service;

import com.musixise.musixisebox.api.admin.vo.common.MusixiserVO;
import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/5/10.
 */
@Api(value = "用户基本信息", description = "用户基本信息管理", tags = {"后台接口"})
public interface MusixiserApi {

    @ApiOperation(value = "获取基本信息列表")
    MusixisePageResponse<List<MusixiserVO>> getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size);


    @ApiOperation(value = "修改基本信息")
    MusixiseResponse updateMusixser(@Valid @RequestBody MusixiserVO musixiserVO);

    @ApiOperation(value = "获取单条信息")
    MusixiseResponse<MusixiserVO> getMusixiser(@PathVariable Long id);

    @ApiOperation(value = "删除一条信息")
    MusixiseResponse delMusixiser(@PathVariable Long id);
}
