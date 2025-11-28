package com.icetea.MonStu.shared.cache.objects;

import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import lombok.Builder;

import java.util.Objects;

@Builder
public class HistoryCacheKey {

    private final String originalText;

    private final LanguageCode sourceLang;
    private final LanguageCode targetLang;

    private final Genre genre;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || this.getClass() != o.getClass()) return false;

        HistoryCacheKey that = (HistoryCacheKey) o;
        return Objects.equals(originalText, that.originalText)
                && sourceLang == that.sourceLang
                && targetLang == that.targetLang
                && genre == that.genre;
    }

    @Override
    public int hashCode() {
        return Objects.hash(originalText, sourceLang, targetLang, genre);
    }
}
