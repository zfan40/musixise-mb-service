package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created by zhaowei on 2018/4/1.
 */
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findOneByLogin(String login);

    User findByLogin(String login);

    Optional<User> findByLoginOrEmail(String login, String email);

    User findByEmail(String email);
}
