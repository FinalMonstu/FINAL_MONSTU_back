package com.icetea.MonStu.api.v2.dto.response;

import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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

    // 엔티티 -> DTO 변환용 정적 팩토리 메서드
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
