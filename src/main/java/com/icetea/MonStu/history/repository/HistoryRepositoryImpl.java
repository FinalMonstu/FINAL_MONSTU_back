package com.icetea.MonStu.history.repository;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.icetea.MonStu.history.domain.QHistory.history;

@RequiredArgsConstructor
public class HistoryRepositoryImpl implements HistoryRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Optional<History> findExisting(String text, LanguageCode src, LanguageCode tgt, Genre genre) {
        return Optional.ofNullable(
                queryFactory
                        .selectFrom(history)
                        .where(
                                history.originalText.eq(text),
                                history.sourceLang.eq(src),
                                history.targetLang.eq(tgt),
                                history.genre.eq(genre)
                        )
                        .fetchOne()
        );
    }
}
