package com.icetea.MonStu.history.domain;

import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.shared.common.enums.Genre;
import com.icetea.MonStu.shared.common.enums.LanguageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(exclude = {"posts"})
@Table(name = "history", indexes = {
        @Index(name = "idx_history_lookup", columnList = "original_text, source_lang, target_lang, genre")
})
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;


    @Column @NotBlank
    private String originalText;

    @Column @NotBlank
    private String translatedText;


    @Column @NotNull
    @Enumerated(EnumType.STRING)
    private LanguageCode sourceLang;

    @Column @NotNull
    @Enumerated(EnumType.STRING)
    private LanguageCode targetLang;


    @Column @NotNull
    @Enumerated(EnumType.STRING)
    private Genre genre;


    @Builder.Default
    @ManyToMany(mappedBy = "histories")
    private Set<Post> posts = new HashSet<>();

}
