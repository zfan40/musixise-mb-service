package com.musixise.musixisebox.service;

import com.musixise.musixisebox.controller.vo.resp.work.WorkVO;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface WorkService {

    WorkVO getListByUid(Long uid);

    void updateFavoriteCount(Long workId);
}
