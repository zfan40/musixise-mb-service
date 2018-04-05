package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.domain.Work;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface WorkRepository extends JpaRepository<Work, Long> {
    // PageRequest request = new PageRequest(pageNumber - 1, PAGE_SIZE, Sort.Direction.DESC, "startTime");
    Page<Work> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    List<Work> findAllByUserIdOrderByIdDesc(Long userId);

    @Transactional
    @Modifying
    @Query("update Work w set w.status = ?1 where w.id = ?2 and w.userId=?3")
    int updateStatusByUserIdAndWorkId(Integer status, Long workId, Long userId);

    @Transactional
    @Modifying
    @Query("update Work w set w.collectNum = ?2 where w.id = ?1 ")
    int updateCollectNumById(Long id, Integer collectNum);

    int countByUserId(Long userId);

    List<Work> findTop10ByOrderByPvDesc();

    List<Work> findTop10ByOrderByIdDesc();

    @Transactional
    @Modifying
    @Query("update Work w set w.pv = w.pv+1 where w.id = ?1 ")
    int updatePvById(Long id);

}
