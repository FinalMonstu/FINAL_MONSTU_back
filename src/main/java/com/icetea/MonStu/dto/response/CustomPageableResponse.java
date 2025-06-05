package com.icetea.MonStu.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
@Builder
public class CustomPageableResponse<T> {
    private final List<T> content;
    private final long totalElements;
    private final int totalPages;
    private final int number;
    private final int size;

    public static <T> CustomPageableResponse<T> mapper(Page<T> page) {
        return CustomPageableResponse.<T>builder()
                .content(page.getContent())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .number(page.getNumber())
                .size(page.getSize())
                .build();
    }
}

