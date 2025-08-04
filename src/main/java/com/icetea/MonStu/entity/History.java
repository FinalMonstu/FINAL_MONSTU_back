package com.icetea.MonStu.entity;

import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.HashSet;
import java.util.Set;


@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = {"posts"})
@Table(name="history")
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
