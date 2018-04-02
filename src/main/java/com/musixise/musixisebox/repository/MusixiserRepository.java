package com.musixise.musixisebox.repository;

import com.musixise.musixisebox.domain.Musixiser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by zhaowei on 2018/4/1.
 */
public interface MusixiserRepository extends JpaRepository<Musixiser, Long> {
    Musixiser findOneByUserId(Long userId);
}
