package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.domain.Favorite;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/5.
 */
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Page<Favorite> findAllByUserIdOrderByIdDesc(Long userId, Pageable pageable);

    @Query(value="select * from mu_work_list_follow w where w.user_id = :userId and w.work_id= :workId", nativeQuery = true)
    Optional<Favorite> findOneByUserIdAndWorkId(@Param("userId") Long userId, @Param("workId") Long workId);

    @Modifying
    @Transactional
    @Query("delete Favorite where user_id=?1 and work_id  = ?2")
    int deleteByUserIdAndWorkId(Long userId,  Long workId);

    int countByWorkId(Long workId);
}
