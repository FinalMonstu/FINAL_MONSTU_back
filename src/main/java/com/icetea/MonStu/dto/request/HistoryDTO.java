package com.icetea.MonStu.dto.request;

import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HistoryDTO {
    private Long id;
    private String target;  //단어,문장
    private Genre genre;    //WORD,SENTENCE
    private LanguageCode languageCode;
}
