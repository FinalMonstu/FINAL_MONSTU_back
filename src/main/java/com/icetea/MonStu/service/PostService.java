package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.request.post.PostFilterRequest;
import com.icetea.MonStu.dto.request.post.PostRequest;
import com.icetea.MonStu.dto.request.post.UpdatePostRequest;
import com.icetea.MonStu.dto.response.post.PostLiteResponse;
import com.icetea.MonStu.dto.response.post.PostResponse;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.mapper.PostMapper;
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


    @Transactional
    public PostResponse create(PostRequest request, Long userId) {
        return memberRps.findById( userId )
                .map(member-> postRps.save(PostMapper.toEntity(request,member)))
                .map(PostResponse::toDto)
                .orElseThrow(()->new NoSuchElementException("회원 정보를 찾을 수 없습니다"));
    }

    // Pageable과 Id 이용, 회원이 작성한 모든 게시물 목록 반환
    public Page<PostLiteResponse> getMyPosts(Long userId, Pageable pageable) {
        return postRps.findByMember_Id(userId, pageable)
                .map(PostLiteResponse::toDto);
    }

    // Pageable 이용, 모든 공개 게시물 반환
    public Page<PostLiteResponse> getPublicPosts(Pageable pageable) {
        return postRps.findByIsPublicTrue(pageable)
                .map( PostLiteResponse::toDto );
    }

    // ID 사용, 게시물 반환
    public PostResponse getById(Long id) {
        return postRps.findById(id)
                .map(PostResponse::toDto)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // Pageable과 전달 받은 필터 정보를 이용, 필터링된 게시물 목록 반환
    public Page<PostResponse> filter(PostFilterRequest filter, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildPostsFilterPredicate(filter);
        return postRps.findAll(predicate, pageable)
                .map(com.icetea.MonStu.dto.response.post.PostResponse::toDto);
    }

    // 게시글 ID를 이용, 게시글 & 로그 정보 반환
    public PostResponse findWithMemberAndLogById(Long id) {
        return postRps.findWithMemberAndLogById(id)
                .map(com.icetea.MonStu.dto.response.post.PostResponse::toDto)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 게시글 ID 사용, 게시글 수정
    @Transactional
    public void update(UpdatePostRequest request) {
        Post post = postRps.findById(request.id())
                .orElseThrow(()-> new NoSuchElementException(null));
        PostMapper.updateFromDto(post, request);
    }

    // ID 사용, 게시물 삭제
    @Transactional
    public void deleteById(Long id) {
        Post post = postRps.findById(id)
                .orElseThrow(() -> new NoSuchElementException("게시물이 없습니다: " + id));
        postRps.delete(post);
    }

    // 게시글 ID 목록 사용, 여러 게시물 삭제
    @Transactional
    public void deletePosts(List<Long> ids) { postRps.deleteAllByIdInBatch(ids); }


}
