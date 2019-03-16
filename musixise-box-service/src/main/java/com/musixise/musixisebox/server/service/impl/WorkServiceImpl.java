package com.musixise.musixisebox.server.service.impl;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Favorite;
import com.musixise.musixisebox.server.domain.Follow;
import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.manager.FavoriteManager;
import com.musixise.musixisebox.server.manager.FollowManager;
import com.musixise.musixisebox.server.manager.UserManager;
import com.musixise.musixisebox.server.repository.FavoriteRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.WorkService;
import com.musixise.musixisebox.server.transfter.UserTransfter;
import com.musixise.musixisebox.server.transfter.WorkTransfter;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by zhaowei on 2018/4/5.
 */
@Component
public class WorkServiceImpl implements WorkService {

    @Resource WorkRepository workRepository;

    @Resource FavoriteRepository favoriteRepository;

    @Resource UserManager userManager;

    @Resource FollowManager followManager;

    @Resource FavoriteManager favoriteManager;

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

    @Override
    public List<WorkVO> getWorkList(List<Work> workList) {

        //get work ids
        List<Long> userIds = workList.stream().map(Work::getUserId).collect(Collectors.toList());
        List<Long> workIds = workList.stream().map(Work::getId).collect(Collectors.toList());
        Map<Long, Musixiser> musixiserMap = userManager.getMusixiserMap(userIds);

        Map<Long, Follow> followsMap = followManager.getFollows(userIds);
        Map<Long, Favorite> favoriteMap = favoriteManager.getFavorites(workIds);

        List<WorkVO> workVOList = new ArrayList<>();

        workList.forEach( w -> {
            WorkVO workVO = WorkTransfter.getWorkVO(w);
            UserVO userVO = UserTransfter.getUserVO(musixiserMap.get(w.getUserId()));
            //是否关注
            if (followsMap.containsKey(w.getUserId())) {
                userVO.setFollowStatus(1);
            } else {
                userVO.setFollowStatus(0);
            }

            //是否收藏
            if (favoriteMap.containsKey(w.getId())) {
                workVO.setFavStatus(1);
            } else {
                workVO.setFavStatus(0);
            }
            workVO.setUserVO(userVO);
            workVOList.add(workVO);
        });


        return workVOList;

    }


}
