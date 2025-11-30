package com.icetea.MonStu.post.application;

import com.icetea.MonStu.member.domain.Member;
import com.icetea.MonStu.post.dto.v2.request.CreatePostRequest;
import com.icetea.MonStu.post.dto.v2.request.FilterPostRequest;
import com.icetea.MonStu.post.dto.v2.request.UpdatePostRequest;
import com.icetea.MonStu.post.dto.v2.response.PostResponse;
import com.icetea.MonStu.post.dto.v2.response.PostSummaryResponse;
import com.icetea.MonStu.post.domain.Post;
import com.icetea.MonStu.shared.exception.EmptyParameterException;
import com.icetea.MonStu.shared.exception.ForbiddenException;
import com.icetea.MonStu.shared.exception.NoSuchElementException;
import com.icetea.MonStu.member.repository.MemberRepository;
import com.icetea.MonStu.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRps;
    private final MemberRepository memberRps;

    @Transactional
    public PostResponse createPost(CreatePostRequest createPostRequest, Long userId) {
        return memberRps.findById( userId )
                .map(member-> postRps.save(PostMapper.toEntity(createPostRequest,member)))
                .map(PostResponse::toDto)
                .orElseThrow(()->new NoSuchElementException("회원 정보를 찾을 수 없습니다"));
    }


    // Pageable과 Id 이용, 회원이 작성한 모든 게시물 목록 반환
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getMyPostSummaries(Long userId, Pageable pageable) {
        return postRps.findMyPostSummaries(userId, pageable);
    }

    // Pageable 이용, 모든 공개 게시물 반환
    @Transactional(readOnly = true)
    public Page<PostSummaryResponse> getPublicPosts(Pageable pageable) {
        return postRps.findPublicPostSummaries(pageable);
    }

    // Pageable과 전달 받은 필터 정보를 이용, 필터링된 게시물 목록 반환
    @Transactional(readOnly = true)
    public Page<PostResponse> getFilteredPosts(FilterPostRequest postFilter, Pageable pageable) {
        Page<Post> postPage = postRps.findAllByFilter(postFilter, pageable);
        return postPage.map(PostResponse::toDto);
    }

    // 게시글 ID를 이용, 게시글 & 로그 정보 반환
    @Transactional(readOnly = true)
    public PostResponse findPostWithLogById(Long id) {
        return postRps.readPostWithLogById(id)
                .map(PostResponse::toDto)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 게시글 ID 사용, 게시글 수정
    @Transactional
    public void updatePost(Long postId,UpdatePostRequest request) {
        Post post = postRps.findById(postId)
                .orElseThrow(()-> new NoSuchElementException("게시글을 찾을 수 없습니다."));

        post.update(request.title(), request.content(), request.isPublic());
    }

    // [사용자용] ID 사용, 게시물 삭제
    @Transactional
    public void deleteById(Long postId, Long requesterId) {
        Post post = postRps.findById(postId)
                .orElseThrow(() -> new NoSuchElementException("게시물이 없습니다: " + postId));

        if (!post.isOwner(requesterId)) throw new ForbiddenException("본인의 게시글만 수정/삭제할 수 있습니다.");

        postRps.delete(post);
    }

    // 게시글 ID 목록 사용, 여러 게시물 삭제
    @Transactional
    public void deletePosts(List<Long> ids) {
        if (ids == null || ids.isEmpty())  throw new EmptyParameterException("삭제할 목록이 비어있습니다.");
        postRps.deleteAllByIdInBatch(ids);
    }


}