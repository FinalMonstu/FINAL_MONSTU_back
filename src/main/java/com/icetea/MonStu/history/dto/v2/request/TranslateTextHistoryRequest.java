package com.icetea.MonStu.history.dto.v2.request;

import com.icetea.MonStu.history.domain.History;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TranslateTextHistoryRequest(

        @NotNull(message = "{PostRequest.authorEmail.NotNull}")
        Long postId,

        @NotEmpty
        List<History> historyList
) { }
