package com.icetea.MonStu.service;

import com.icetea.MonStu.api.v2.dto.request.CreatePostRequest;
import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.api.v2.dto.request.UpdatePostRequest;
import com.icetea.MonStu.api.v2.dto.response.PostResponse;
import com.icetea.MonStu.api.v2.dto.response.PostSummaryResponse;
import com.icetea.MonStu.api.v2.mapper.PostMapper;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.exception.EmptyParameterException;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.repository.PostRepository;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRps;
    private final MemberRepository memberRps;
    private final FilterPredicateManager filterPredicateManager;

    @Transactional
    public PostResponse createPost(CreatePostRequest createPostRequest, Long userId) {
        return memberRps.findById( userId )
                .map(member-> postRps.save(PostMapper.toEntity(createPostRequest,member)))
                .map(PostResponse::toDto)
                .orElseThrow(()->new NoSuchElementException("회원 정보를 찾을 수 없습니다"));
    }

    // Pageable과 Id 이용, 회원이 작성한 모든 게시물 목록 반환
    public Page<PostSummaryResponse> getMyPostSummaries(Long userId, Pageable pageable) {
        return postRps.findMyPostSummaries(userId, pageable);
    }

    // Pageable 이용, 모든 공개 게시물 반환
    public Page<PostSummaryResponse> getPublicPosts(Pageable pageable) {
        return postRps.findPublicPostSummaries(pageable);
    }

    // ID 사용, 게시물 반환
    public PostResponse getPostById(Long postId) {
        return postRps.findPostWithLogById(postId)
                .map(PostResponse::toDto)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // Pageable과 전달 받은 필터 정보를 이용, 필터링된 게시물 목록 반환
    public Page<PostResponse> getFilteredPosts(FilterPostRequest postFilter, Pageable pageable) {
        Page<Post> postPage = postRps.findAllByFilter(postFilter, pageable);
        return postPage.map(PostResponse::toDto);
    }

    // 게시글 ID를 이용, 게시글 & 로그 정보 반환
    public PostResponse findPostWithLogById(Long id) {
        return postRps.findPostWithLogById(id)
                .map(PostResponse::toDto)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 게시글 ID 사용, 게시글 수정
    @Transactional
    public void updatePost(UpdatePostRequest updatePostRequest) {
        Post post = postRps.findById(updatePostRequest.id())
                .orElseThrow(()-> new NoSuchElementException(null));
        PostMapper.updateFromDto(post, updatePostRequest);
    }

    // ID 사용, 게시물 삭제
    @Transactional
    public void deleteById(Long id) {
        if (id == null)  throw new EmptyParameterException(null);
        Post post = postRps.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시물이 없습니다: " + id));
        postRps.delete(post);
    }

    // 게시글 ID 목록 사용, 여러 게시물 삭제
    @Transactional
    public void deletePosts(List<Long> ids) {
        if (ids == null || ids.isEmpty())  throw new EmptyParameterException(null);
        postRps.deleteAllByIdInBatch(ids);
    }


}