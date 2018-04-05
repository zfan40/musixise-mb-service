package com.musixise.musixisebox.controller;

import com.alibaba.fastjson.JSONObject;
import com.musixise.musixisebox.aop.AppMethod;
import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.favorite.FavoriteVO;
import com.musixise.musixisebox.domain.Favorite;
import com.musixise.musixisebox.domain.result.ExceptionMsg;
import com.musixise.musixisebox.domain.result.ResponseData;
import com.musixise.musixisebox.repository.FavoriteRepository;
import com.musixise.musixisebox.service.FavoriteService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.service.WorkService;
import com.musixise.musixisebox.transfter.FavoriteTransfter;
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
@RequestMapping("/api/favorite")
public class FavoriteController {

    @Resource FavoriteRepository favoriteRepository;

    @Resource FavoriteService favoriteService;

    @Resource WorkService workService;

    @Resource UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @AppMethod(isLogin = true)
    public ResponseData create(Long uid,
                               @RequestParam(value = "workId", defaultValue = "0") Long workId,
                               @RequestParam(value = "status", defaultValue = "1") Integer status) {

        Favorite favorite = favoriteRepository.getOne(workId);
        if (favorite != null) {
            if (status == 1) {
                favoriteService.create(uid, workId);
            } else if (status == 0) {
                favoriteService.cancle(uid, workId);
            } else {
                return new ResponseData(ExceptionMsg.PARAM_ERROR);
            }
            //更新收藏数
            workService.updateFavoriteCount(workId);
            return new ResponseData(ExceptionMsg.SUCCESS);
        } else {
            return new ResponseData(ExceptionMsg.NOT_EXIST);
        }
    }

    @RequestMapping(value = "/getWorkList/{uid}", method = RequestMethod.GET)
    @AppMethod
    public ResponseData getList(@RequestParam(value = "uid", defaultValue = "0") Long uid,
                                @RequestParam(value = "page", defaultValue = "1") Integer page,
                                @RequestParam(value = "size", defaultValue = "10") Integer size) {
        Sort sort = new Sort(Sort.Direction.DESC, "id");
        Pageable pageable = new PageRequest(page, size, sort);

        Page<Favorite> favorites = favoriteRepository.findAllByUserIdOrderByIdDesc(uid, pageable);

        List<FavoriteVO> favoriteVOList = new ArrayList<>();
        favorites.forEach(favorite -> {

            UserVO userVO = userService.getById(favorite.getUserId());
            favoriteVOList.add(FavoriteTransfter.getFavoriteWithUser(favorite, userVO));
        });

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("total", favorites.getTotalElements());
        jsonObject.put("list", favoriteVOList);
        jsonObject.put("size", size);
        jsonObject.put("current", page);

        return new ResponseData(ExceptionMsg.SUCCESS, jsonObject);
    }

}
