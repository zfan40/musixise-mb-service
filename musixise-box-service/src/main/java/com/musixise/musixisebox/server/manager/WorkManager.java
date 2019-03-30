package com.musixise.musixisebox.server.manager;

import com.musixise.musixisebox.api.web.vo.resp.UserVO;
import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.QWork;
import com.musixise.musixisebox.server.domain.Work;
import com.musixise.musixisebox.server.repository.MusixiserRepository;
import com.musixise.musixisebox.server.repository.WorkRepository;
import com.musixise.musixisebox.server.service.FavoriteService;
import com.musixise.musixisebox.server.service.FollowService;
import com.musixise.musixisebox.server.service.MusixiseService;
import com.musixise.musixisebox.server.service.UserService;
import com.musixise.musixisebox.server.transfter.WorkTransfter;
import com.querydsl.core.BooleanBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    public Page<Work> getRecommends(int page, int limit) {

        QWork work = QWork.work;
        //BooleanBuilder booleanBuilder = new BooleanBuilder();
        //booleanBuilder.and(work.id.in(workIds));
        Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC,"pv"),
                new Sort.Order(Sort.Direction.DESC, "lastModifiedDate"));
        //Predicate predicate = work.id.longValue().lt(3);
        PageRequest pageRequest = new PageRequest(page-1,limit,sort);

        Pageable pageable = PageRequest.of(page-1, limit, sort);
        return workRepository.findAll(pageRequest);
    }
}
