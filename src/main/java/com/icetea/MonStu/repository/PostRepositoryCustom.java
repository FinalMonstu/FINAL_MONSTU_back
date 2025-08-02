package com.icetea.MonStu.repository;

import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.entity.Post;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    // 필터링된 데이터의 ID들 조회
    List<Long> findPostIdsByFilter(FilterPostRequest filterDTO, Pageable pageable);

    // 필터링된 데이터의 갯수 조회
    long countPostsByFilter(FilterPostRequest filterDTO);

    // ids 목록에 해당하는 Posts 조회
    List<Post> findPostsByIds(List<Long> ids);

}
