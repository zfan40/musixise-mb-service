package com.musixise.musixisebox.service.impl;

import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.repository.FavoriteRepository;
import com.musixise.musixisebox.repository.WorkRepository;
import com.musixise.musixisebox.service.WorkService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class WorkServiceImpl implements WorkService {

    @Resource WorkRepository workRepository;

    @Resource FavoriteRepository favoriteRepository;

    @Override
    public WorkVO getListByUid(Long uid) {
        return null;
    }

    @Async
    @Transactional
    @Override
    public void updateFavoriteCount(Long workId) {
        int favNum = favoriteRepository.countByWorkId(workId);
        workRepository.updateCollectNumById(workId, favNum);
    }
}
