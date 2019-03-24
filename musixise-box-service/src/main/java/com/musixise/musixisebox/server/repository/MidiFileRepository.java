package com.musixise.musixisebox.server.repository;

import com.musixise.musixisebox.server.domain.MidiFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MidiFileRepository extends JpaRepository<MidiFile, Long>, QuerydslPredicateExecutor<MidiFile> {

    Optional<MidiFile> findByMd5(String md5);
    int deleteByMd5(String md5);
}
