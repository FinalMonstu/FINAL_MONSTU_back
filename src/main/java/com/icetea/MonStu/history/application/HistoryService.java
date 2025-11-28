package com.icetea.MonStu.history.application;

import com.icetea.MonStu.post.dto.v2.request.PostHistoryLinkRequest;
import com.icetea.MonStu.history.dto.v2.HistoryResponse;
import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.history.repository.HistoryRepository;
import com.icetea.MonStu.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PostRepository postRps;
    private final HistoryRepository historyRps;

    @Transactional
    public void linkPostWithHistories(PostHistoryLinkRequest postHistoryLinkRequest) {
        Long       postId     = postHistoryLinkRequest.postId();
        List<Long> historyIds = postHistoryLinkRequest.historyIds();

        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        List<History> savedHistories = historyRps.findAllById(historyIds);

        savedHistories.forEach(post::addHistory);
    }

    public List<HistoryResponse> getHistoriesByPost(Long postId) {
        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        return post.getHistories().stream()
                .map(HistoryResponse::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void unlinkHistoryFromPost(Long historyId, Long postId) {
        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        History history = historyRps.findById(historyId)
                .orElseThrow(() -> new IllegalArgumentException("History not found"));

        post.removeHistory(history);
    }
}
