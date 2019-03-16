package com.musixise.musixisebox.server.manager;

import com.musixise.musixisebox.server.aop.MusixiseContext;
import com.musixise.musixisebox.server.domain.Favorite;
import com.musixise.musixisebox.server.domain.QFavorite;
import com.musixise.musixisebox.server.repository.FavoriteRepository;
import com.querydsl.core.BooleanBuilder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class FavoriteManager {

    @Resource FavoriteRepository favoriteRepository;

    public Map<Long, Favorite> getFavorites(List<Long> workId) {

        QFavorite qFavorite = QFavorite.favorite;
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        booleanBuilder.and(qFavorite.userId.eq(MusixiseContext.getCurrentUid()));
        booleanBuilder.and(qFavorite.workId.in(workId));
        Iterable<Favorite> worksIt = favoriteRepository.findAll(booleanBuilder);

        return StreamSupport.stream(worksIt.spliterator(), false).collect(Collectors.toMap(Favorite::getWorkId, f -> f));

    }
}
