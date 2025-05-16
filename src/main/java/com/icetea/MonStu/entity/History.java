package com.icetea.MonStu.entity;

import com.icetea.MonStu.entity.link.MemberPostHistory;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"memberPostHistories"})
@Entity
@Table(name="history")
public class History {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column @NotBlank
    private String target;      // 단어 또는 문장

    @Column @NotBlank
    @Enumerated(EnumType.STRING)
    private Genre genre;        //장르

    @Column @NotBlank
    @Enumerated(EnumType.STRING)
    private LanguageCode languageCode;

    @Builder.Default
    @OneToMany(mappedBy = "history", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MemberPostHistory> memberPostHistories = new ArrayList<>();;
}
