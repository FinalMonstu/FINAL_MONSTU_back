package com.icetea.MonStu.history.repository;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;

import java.util.Optional;

public interface HistoryRepositoryCustom {

    // 이미 존재하는지 확인하는 메서드
    Optional<History> findExisting(String text, LanguageCode src, LanguageCode tgt, Genre genre);

}
