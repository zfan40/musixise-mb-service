package com.musixise.musixisebox.server.repository;

import com.musixise.musixisebox.server.domain.UserBind;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/5.
 */
public interface UserBindRepository extends JpaRepository<UserBind, Long> {
    UserBind findByOpenId(String openId);
    UserBind findByOpenIdAndProvider(String openId, String provider);
    List<UserBind> findAllByLogin(String login);
    Optional<UserBind> findByUserIdAndProvider(Long userId, String provider);
}
