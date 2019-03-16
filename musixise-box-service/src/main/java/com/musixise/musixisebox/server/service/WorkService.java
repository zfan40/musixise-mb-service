package com.musixise.musixisebox.server.service;


import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;
import com.musixise.musixisebox.server.domain.Work;

import java.util.List;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface WorkService {

    WorkVO getListByUid(Long uid);

    void updateFavoriteCount(Long workId);

    List<WorkVO> getWorkList(List<Work> workList);

}
