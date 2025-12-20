package com.icetea.MonStu.history.repository;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface HistoryRepository extends MongoRepository<History, String> {

    // 짧은 이름의 Wrapper 메소드 (findByOriginalTextAndSourceLangAndTargetLangAndGenre()를 Wrapping)
    default Optional<History> findHistory(String text, LanguageCode src, LanguageCode tgt, Genre genre) {
        return findByOriginalTextAndSourceLangAndTargetLangAndGenre(text, src, tgt, genre);
    }

    Optional<History> findByOriginalTextAndSourceLangAndTargetLangAndGenre(
            String originalText,
            LanguageCode sourceLang,
            LanguageCode targetLang,
            Genre genre
    );
}
