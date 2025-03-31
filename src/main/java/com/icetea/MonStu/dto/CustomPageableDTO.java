package com.icetea.MonStu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY) //null,빈값 제외
public class CustomPageableDTO {
    private int page = 0;
    private int size = 6;

    private String sortValue;
    private String sortDirection;

    private String filterOption;
    private String filterValue;

    private String dateOption;
    private LocalDateTime dateStart;
    private LocalDateTime dateEnd;
}
