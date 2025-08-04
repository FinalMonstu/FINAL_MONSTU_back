package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.request.TranslateTextHistoryRequest;
import com.icetea.MonStu.api.v2.dto.response.HistoryResponse;
import com.icetea.MonStu.entity.History;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.enums.Genre;
import com.icetea.MonStu.enums.LanguageCode;
import com.icetea.MonStu.repository.HistoryRepository;
import com.icetea.MonStu.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PostRepository postRps;
    private final HistoryRepository historyRps;

    @Transactional
    public void saveTranslateTextHistory(TranslateTextHistoryRequest translateTextHistoryRequest) {
        Post post = postRps.findById(translateTextHistoryRequest.postId())
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        List<History> histories      = translateTextHistoryRequest.historyList();
        List<History> savedHistories = historyRps.saveAll(histories);

        for (History history : savedHistories) { post.addHistory(history); }
    }

    public List<HistoryResponse> getHistoriesByPost(Long postId) {
        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        return post.getHistories().stream()
                .map(HistoryResponse::toDto)
                .collect(Collectors.toList());
    }

}
