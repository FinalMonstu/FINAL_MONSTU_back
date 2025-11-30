package com.icetea.MonStu.history.dto.v2.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record PostHistoryLinkRequest(
        @NotNull
        Long postId,

        @NotEmpty
        List<Long> historyIds
) {
}
