package com.musixise.musixisebox.manager;

import com.musixise.musixisebox.controller.vo.resp.UserVO;
import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;
import com.musixise.musixisebox.domain.Work;
import com.musixise.musixisebox.service.FavoriteService;
import com.musixise.musixisebox.service.FollowService;
import com.musixise.musixisebox.service.UserService;
import com.musixise.musixisebox.transfter.WorkTransfter;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Created by zhaowei on 2018/4/15.
 */
@Component("workManager")
public class WorkManager {

    @Resource UserService userService;

    @Resource FavoriteService favoriteService;

    @Resource FollowService followService;

    public WorkVO getWorkVO(Long uid, Work work) {
        UserVO userVO = userService.getById(work.getUserId());
        Boolean isFavorite = favoriteService.isFavorite(uid, work.getId());
        Boolean isFollow = followService.isFollow(uid, work.getUserId());
        userVO.setFollowStatus(isFollow ? 1 : 0);
        return WorkTransfter.getWorkVO(work, userVO, isFavorite);
    }
}
