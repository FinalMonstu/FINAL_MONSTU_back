package com.icetea.MonStu.history.application;

import com.icetea.MonStu.history.dto.v2.request.PostHistoryLinkRequest;
import com.icetea.MonStu.history.dto.v2.HistoryResponse;
import com.icetea.MonStu.history.domain.History;
import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.history.repository.HistoryRepository;
import com.icetea.MonStu.post.repository.PostRepository;
import com.icetea.MonStu.shared.exception.ForbiddenException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final PostRepository postRps;
    private final HistoryRepository historyRps;

    // Post와 History를 연결,저장
    @Transactional
    public void linkPostWithHistories(PostHistoryLinkRequest postHistoryLinkRequest) {
        Long       postId     = postHistoryLinkRequest.postId();
        List<String> historyIds = postHistoryLinkRequest.historyIds();

        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다"));

        List<History> savedHistories = historyRps.findAllById(historyIds);

        savedHistories.stream()
                    .map(History::getId)
                    .forEach(post::addHistory);
    }

    // 특정 Post에 연결된 모든 History를 조회
    public List<HistoryResponse> getHistoriesByPost(Long postId) {

        Set<String> historyIds = getHistoryIdsOnly(postId);

        if (historyIds.isEmpty()) return Collections.emptyList();

        List<History> histories = historyRps.findAllById(historyIds);

        return histories.stream()
                .map(HistoryResponse::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Set<String> getHistoryIdsOnly(Long postId) {
        Post post = postRps.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시물 없음"));

        return new HashSet<>(post.getHistoryIds());
    }

    @Transactional
    public void unlinkHistoryFromPost(String historyId, Long postId, Long requesterId) {
        Post post = postRps.readPostWithMemberById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        if (!Objects.equals(post.getMember().getId(), requesterId)) {
            throw new ForbiddenException("본인의 게시글만 삭제할 수 있습니다.");
        }

        post.removeHistory(historyId);
    }
}
