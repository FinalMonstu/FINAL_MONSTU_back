package com.icetea.MonStu.history.dto.v2;

import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder
@ToString
public class HistoryResponse {

    private Long id;

    private String originalText;
    private String translatedText;

    private LanguageCode sourceLang;
    private LanguageCode targetLang;

    private Genre genre;


    public static HistoryResponse toDto(History history) {
        return HistoryResponse.builder()
                .id             (history.getId())
                .originalText   (history.getOriginalText())
                .translatedText (history.getTranslatedText())
                .sourceLang     (history.getSourceLang())
                .targetLang     (history.getTargetLang())
                .genre          (history.getGenre())
                .build();
    }

}
