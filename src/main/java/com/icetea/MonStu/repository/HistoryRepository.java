package com.icetea.MonStu.repository;

import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HistoryRepository extends JpaRepository<History,Long> {

    Optional<History> findByOriginalTextAndSourceLangAndTargetLangAndGenre(
            String originalText, LanguageCode sourceLang, LanguageCode targetLang, Genre genre);
}
