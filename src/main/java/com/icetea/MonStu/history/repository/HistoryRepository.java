package com.icetea.MonStu.history.repository;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {

    Optional<History> findByOriginalTextAndSourceLangAndTargetLangAndGenre(
            String originalText, LanguageCode sourceLang, LanguageCode targetLang, Genre genre);
}
