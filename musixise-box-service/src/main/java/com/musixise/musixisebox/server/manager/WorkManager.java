package com.musixise.musixisebox.server.manager;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.QWork;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.*;
import com.musixise.musixisebox.server.transfter.WorkTransfter;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Created by zhaowei on 2018/4/15.
 */
@Component("workManager")
public class WorkManager {

    @Resource UserService userService;

    @Resource FavoriteService favoriteService;

    @Resource WorkRepository workRepository;

    @Resource FollowService followService;

    @Resource MusixiserRepository musixiserRepository;

    @Resource MusixiseService musixiseService;

    @Resource WorkService workService;

    public WorkVO getWorkVO(Long uid, Work work) {
        UserVO userVO = userService.getById(work.getUserId(), false);
        Boolean isFavorite = favoriteService.isFavorite(uid, work.getId());
        Boolean isFollow = followService.isFollow(uid, work.getUserId());
        userVO.setFollowStatus(isFollow ? 1 : 0);
        return WorkTransfter.getWorkVO(work, userVO, isFavorite);
    }

    public Map<Long, Work> getListById(List<Long> workIds) {
        QWork work = QWork.work;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(work.id.in(workIds));
        Iterable<Work> worksIt = workRepository.findAll(booleanBuilder);
        return StreamSupport.stream(worksIt.spliterator(), false).collect(Collectors.toMap(Work::getId, m -> m));
    }

    public List<Work> getWorkList(List<Long> workIds) {
        return workRepository.findAllById(workIds);
    }
}
