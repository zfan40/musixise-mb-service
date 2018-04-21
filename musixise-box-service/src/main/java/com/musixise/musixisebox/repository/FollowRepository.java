package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.domain.Follow;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/4.
 */
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Page<Follow> findAllByUserId(Pageable pageable, Long userId);

    Page<Follow> findAllByFollowId(Pageable pageable, Long userId);

    @Modifying
    @Transactional
    @Query("delete Follow where user_id=?1 and follow_uid  = ?2")
    int deleteByUserIdAndFollowUid(Long userId,  Long followUid);

    @Query(value="SELECT * FROM `mu_musixiser_follow` m LEFT JOIN mu_user u on u.id=m.follow_uid WHERE m.user_id=?1 and m.follow_uid=?2", nativeQuery = true)
    Follow findByUserIdAndFollowUid(@Param("userId") Long userId, @Param("followId") Long followId);

    int countByUserId(Long userId);

    int countByFollowId(Long followId);

    Optional<Follow> findOneByUserIdAndFollowId(@Param("userId") Long userId, @Param("followId") Long followId);


}
