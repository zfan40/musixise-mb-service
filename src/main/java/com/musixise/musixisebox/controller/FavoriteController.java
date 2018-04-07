package com.musixise.musixisebox.controller;

import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.domain.Favorite;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.MusixisePageResponse;
import com.musixise.musixisebox.domain.result.MusixiseResponse;
import com.musixise.musixisebox.repository.FavoriteRepository;
import com.musixise.musixisebox.service.FavoriteService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.service.WorkService;
import com.musixise.musixisebox.transfter.FavoriteTransfter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhaowei on 2018/4/5.
 */
@RestController
@Api(value = "收藏", description = "收藏操作", tags = "收藏")
@RequestMapping("/api/v1/favorite")
public class FavoriteController {

    @Resource FavoriteRepository favoriteRepository;

    @Resource FavoriteService favoriteService;

    @Resource WorkService workService;

    @Resource UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ApiOperation(value = "添加收藏")
    @ApiImplicitParam(name = "status", value = "操作收藏 (1=收藏，0=取消收藏)", defaultValue = "1", allowableValues="0,1", dataType = "Integer")
    @AppMethod(isLogin = true)
    public MusixiseResponse create(Long uid,
                                   @RequestParam(value = "workId", defaultValue = "0") Long workId,
                                   @RequestParam(value = "status", defaultValue = "1") Integer status) {

        Favorite favorite = favoriteRepository.getOne(workId);
        if (favorite != null) {
            if (status == 1) {
                favoriteService.create(uid, workId);
            } else if (status == 0) {
                favoriteService.cancle(uid, workId);
            } else {
                return new MusixiseResponse(ExceptionMsg.PARAM_ERROR);
            }
            //更新收藏数
            workService.updateFavoriteCount(workId);
            return new MusixiseResponse(ExceptionMsg.SUCCESS);
        } else {
            return new MusixiseResponse(ExceptionMsg.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/getWorkList/{uid}", method = RequestMethod.GET)
    @ApiOperation(value = "获取收藏列表")
    @AppMethod
    public MusixisePageResponse<List<FavoriteVO>> getList(@RequestParam(value = "uid", defaultValue = "0") Long uid,
                                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                                        @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Favorite> favorites = favoriteRepository.findAllByUserIdOrderByIdDesc(uid, pageable);

        List<FavoriteVO> favoriteVOList = new ArrayList<>();
        favorites.forEach(favorite -> {

            UserVO userVO = userService.getById(favorite.getUserId());
            favoriteVOList.add(FavoriteTransfter.getFavoriteWithUser(favorite, userVO));
        });

        return new MusixisePageResponse<>(favoriteVOList, favorites.getTotalElements(), size, page);
    }

}
