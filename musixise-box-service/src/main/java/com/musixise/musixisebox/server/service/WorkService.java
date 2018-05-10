package com.musixise.musixisebox.server.service;


import com.musixise.musixisebox.api.web.vo.resp.work.WorkVO;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface WorkService {

    WorkVO getListByUid(Long uid);

    void updateFavoriteCount(Long workId);
}
