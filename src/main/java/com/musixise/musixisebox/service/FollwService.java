package com.musixise.musixisebox.service;

import java.util.List;

/**
 * Created by zhaowei on 2018/4/3.
 */
public interface FollwService {

    List<Long> followings(Long id);

    void add();

}
