package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.api.v2.dto.response.PostSummaryResponse;
import com.icetea.MonStu.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllByFilter(FilterPostRequest filterDTO, Pageable pageable);

    Page<PostSummaryResponse> findMyPostSummaries(Long userId, Pageable pageable);

    Page<PostSummaryResponse> findPublicPostSummaries(Pageable pageable);

}
