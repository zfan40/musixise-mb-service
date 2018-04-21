package com.musixise.musixisebox.web;

import com.musixise.musixisebox.domain.Musixiser;
import com.musixise.musixisebox.api.enums.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixisePageResponse;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.repository.MusixiserRepository;
import com.musixise.musixisebox.aop.AppMethod;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/1.
 */
@RestController
@Api(value = "用户基本信息", description = "用户基本信息管理", tags = {"后台接口"})
@RequestMapping("/api/v1/admin/")
public class MusixiserController {

    @Resource MusixiserRepository musixiserRepository;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @RequestMapping(value = "/musixisers", method = RequestMethod.GET)
    @ApiOperation(value = "获取基本信息列表")
    @AppMethod(isAdmin = true)
    public MusixisePageResponse<List<Musixiser>> getMusixisers(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(value = "size", defaultValue = "10") Integer size) {

        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page-1, size, sort);
        Page<Musixiser> all = musixiserRepository.findAll(pageable);
        return new MusixisePageResponse<>(all.getContent(), all.getTotalElements(), size, page);
    }

    @RequestMapping(value = "/musixisers", method = RequestMethod.PUT)
    @ApiOperation(value = "修改基本信息")
    @AppMethod(isAdmin = true)
    public MusixiseResponse updateMusixser(@Valid @RequestBody Musixiser musixiser) {
        musixiserRepository.save(musixiser);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.GET)
    @ApiOperation(value = "获取单条信息")
    @AppMethod(isAdmin = true)
    public MusixiseResponse<Musixiser> getMusixiser(@PathVariable Long id) {
        Musixiser musixiser = musixiserRepository.getOne(id);
        return new MusixiseResponse<>(ExceptionMsg.SUCCESS, musixiser);
    }

    @RequestMapping(value = "/musixisers/{id}", method = RequestMethod.DELETE)
    @ApiOperation(value = "删除一条信息")
    @AppMethod(isAdmin = true)
    public MusixiseResponse delMusixiser(@PathVariable Long id) {
        musixiserRepository.deleteById(id);
        return new MusixiseResponse(ExceptionMsg.SUCCESS);
    }


}
