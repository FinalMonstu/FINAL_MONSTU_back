package com.icetea.MonStu.post.repository;

import com.icetea.MonStu.post.dto.v2.request.FilterPostRequest;
import com.icetea.MonStu.post.dto.v2.response.PostSummaryResponse;
import com.icetea.MonStu.post.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllByFilter(FilterPostRequest filterDTO, Pageable pageable);

    Page<PostSummaryResponse> findMyPostSummaries(Long userId, Pageable pageable);

    Page<PostSummaryResponse> findPublicPostSummaries(Pageable pageable);

}
