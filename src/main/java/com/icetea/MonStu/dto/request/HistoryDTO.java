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
    private Long id;    //단어 ID
    private Long memberId;  // 회원 ID
    private Long postId;    //게시물 ID
    private String target;  //단어,문장
    private Genre genre;    //WORD,SENTENCE
    private String language; //언어  (Korean,English)
}
