package com.icetea.MonStu.api.v2.dto.request;

import com.icetea.MonStu.entity.History;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record TranslateTextHistoryRequest(

        @NotNull(message = "{PostRequest.authorEmail.NotNull}")
        Long postId,

        @NotEmpty
        List<History> historyList
) { }
