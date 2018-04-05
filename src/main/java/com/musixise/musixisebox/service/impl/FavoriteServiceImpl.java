package com.musixise.musixisebox.service.impl;

import com.musixise.musixisebox.domain.Favorite;
import com.musixise.musixisebox.repository.FavoriteRepository;
import com.musixise.musixisebox.service.FavoriteService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class FavoriteServiceImpl implements FavoriteService {

    @Resource FavoriteRepository favoriteRepository;

    /**
     * 收藏作品
     * @param uid
     * @param workId
     * @return
     */
    @Override
    public Boolean create(Long uid, Long workId) {
        Favorite one = favoriteRepository.getOne(workId);
        if (one == null) {
            favoriteRepository.save(new Favorite(uid, workId));
            return true;
        } else {
            return false;
        }
    }

    /**
     * 取消收藏
     * @param uid
     * @param workId
     * @return
     */
    @Override
    public Boolean cancle(Long uid, Long workId) {
        Favorite favorite = favoriteRepository.getOne(workId);
        if (favorite == null) {
            return false;
        } else {
            favoriteRepository.delete(favorite);
            return true;
        }
    }
}
