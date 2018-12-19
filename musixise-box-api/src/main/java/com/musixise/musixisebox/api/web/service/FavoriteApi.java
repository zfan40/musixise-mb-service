package com.musixise.musixisebox.api.web.service;

import com.musixise.musixisebox.api.result.MusixisePageResponse;
import com.musixise.musixisebox.api.result.MusixiseResponse;
import com.musixise.musixisebox.api.web.vo.req.favorite.CreateFavoriteVO;
import com.musixise.musixisebox.api.web.vo.resp.favorite.FavoriteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/21.
 */
@Api(value = "收藏", description = "收藏操作", tags = "收藏")
public interface FavoriteApi {

    @ApiOperation(value = "添加收藏")
    @ApiImplicitParam(name = "status", value = "操作收藏 (1=收藏，0=取消收藏)", defaultValue = "1", allowableValues="0,1", dataType = "Integer")
    MusixiseResponse create(@RequestBody @Valid CreateFavoriteVO createFavoriteVO);

    @ApiOperation(value = "获取收藏列表")
    MusixisePageResponse<List<FavoriteVO>> getList(@PathVariable Long uid,
                                                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                          @RequestParam(value = "size", defaultValue = "10") Integer size);
}
