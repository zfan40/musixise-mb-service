package com.musixise.musixisebox.server.repository;

import com.musixise.musixisebox.server.domain.Musixiser;
import com.musixise.musixisebox.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
public interface MusixiserRepository extends JpaRepository<Musixiser, Long> {
    Optional<Musixiser> findOneByUserId(Long userId);

    int deleteByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.followNum = ?2 where w.userId = ?1 ")
    int updateFollowNumById(Long id, Integer followNum);

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.fansNum = ?2 where w.userId = ?1 ")
    int updateFanswNumById(Long id, Integer fansNum);

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.songNum = ?2 where w.userId = ?1 ")
    int updateWorkNumById(Long id, Integer songNum);


    List<Musixiser> findTop10ByOrderByPvDesc();
    List<Musixiser> findTop10ByOrderByIdDesc();

    @Transactional
    @Modifying
    @Query("update Musixiser w set w.pv = w.pv+1 where w.userId = ?1 ")
    int updatePvById(Long id);

}
