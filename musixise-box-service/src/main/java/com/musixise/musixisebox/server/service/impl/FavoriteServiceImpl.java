package com.musixise.musixisebox.server.service.impl;

import com.musixise.musixisebox.server.domain.Favorite;
import com.musixise.musixisebox.server.repository.FavoriteRepository;
import com.musixise.musixisebox.server.service.FavoriteService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Optional;

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
        Optional<Favorite> optional = favoriteRepository.findOneByUserIdAndWorkId(uid, workId);
        if (optional.isPresent()) {
            return false;
        } else {
            favoriteRepository.save(new Favorite(uid, workId));
            return true;
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
        Optional<Favorite> favorite = favoriteRepository.findOneByUserIdAndWorkId(uid, workId);
        if (favorite.isPresent()) {
            favoriteRepository.delete(favorite.get());
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否收藏过
     * @param uid
     * @param workId
     * @return
     */
    @Override
    public Boolean isFavorite(Long uid, Long workId) {
        Optional<Favorite> oneByUserIdAndWorkId = favoriteRepository.findOneByUserIdAndWorkId(uid, workId);
        if (oneByUserIdAndWorkId.isPresent()) {
            return true;
        } else {
            return false;
        }
    }
}
