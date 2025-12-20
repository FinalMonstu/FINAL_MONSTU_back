package com.icetea.MonStu.history.domain;

import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "history")
@CompoundIndex(name = "idx_history_lookup", def = "{'originalText': 1, 'sourceLang': 1, 'targetLang': 1, 'genre': 1}")
public class History {

    @Id
    private String id;

    private String originalText;
    private String translatedText;

    private LanguageCode sourceLang;
    private LanguageCode targetLang;

    private Genre genre;

}