package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.common.PostDTO;
import com.icetea.MonStu.dto.request.PostFilterRequest;
import com.icetea.MonStu.dto.request.PostRequest;
import com.icetea.MonStu.dto.request.UpdatePostRequest;
import com.icetea.MonStu.dto.response.PostLiteResponse;
import com.icetea.MonStu.dto.response.PostResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.enums.PostStatus;
import com.icetea.MonStu.exception.NoSuchElementException;
import com.icetea.MonStu.manager.FilterPredicateManager;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.repository.PostRepository;
import com.icetea.MonStu.security.CustomUserDetails;
import com.querydsl.core.types.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.icetea.MonStu.entity.QPost.post;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRps;
    private final MemberRepository memberRps;


    @Transactional
    public PostDTO savePost(PostRequest request, CustomUserDetails user) {
        Member member = memberRps.findById( user.getId() )
                .orElseThrow(()->new NoSuchElementException("회원 정보를 찾을 수 없습니다"));

        Post post = Post.builder()
                .id( request.id() )
                .title( request.title() )
                .content( request.content() )
                .createdAt( new Date())
                .status( PostStatus.PUBLIC )
                .isPublic( request.isPublic() )
                .member( member )
                .build();
        System.out.println("Post: "+post);
        Post savedPost  = postRps.save(post);

        PostDTO postDTO = PostDTO.mapper(savedPost);
        postDTO.setAuthorEmail( member.getEmail() );

        return postDTO;
    }

    // Pageable | Id 이용, 회원이 작성한 게시물 목록 반환
    public Page<PostLiteResponse> getPosts(Long userId, Pageable pageable) {
        Predicate predicate = post.member.id.eq( userId );

        return postRps.findAll(predicate, pageable)
                .map( PostLiteResponse::mapper );
    }

    // Pageable 이용, 모든 공개 게시물 반환
    public Page<PostLiteResponse> getPublicPosts(Pageable pageable) {
        Predicate predicate = post.isPublic.eq(true);

        return postRps.findAll(predicate, pageable)
                .map( PostLiteResponse::mapper );
    }

    // ID 사용, 게시물 삭제
    @Transactional
    public void deleteBtId(Long id) {
        if (!postRps.existsById(id))  throw new NoSuchElementException("게시물이 존재하지 않습니다");
        postRps.deleteById(id);
    }

    // ID 사용, 게시물 반환
    public PostDTO findById(Long id) {
        Post post = postRps.findById(id)
                .orElseThrow(()-> new NoSuchElementException(null));
        return PostDTO.mapper(post);
    }

    // Pageable과 전달 받은 필터 정보를 이용, 필터링된 게시물 목록 반환
    public Page<PostResponse> filterPosts(PostFilterRequest filter, Pageable pageable) {
        Predicate predicate = FilterPredicateManager.buildPostsFilterPredicate(filter);
        return postRps.findAll(predicate, pageable)
                .map(PostResponse::mapper);
    }

    // 게시글 ID를 이용, 게시글 & 로그 정보 반환
    public PostResponse findWithMemberAndLogById(Long id) {
        return postRps.findWithMemberAndLogById(id)
                .map(PostResponse::mapper)
                .orElseThrow(()-> new NoSuchElementException(null));
    }

    // 게시글 ID 사용, Status를 'DELETE'로 변경
    @Transactional
    public void deletePosts(List<Long> ids) {
        postRps.updateStatusById(ids,PostStatus.DELETED);
    }

    // 게시글 ID 사용, 게시글 수정
    @Transactional
    public void updatePost(UpdatePostRequest request) {
        Post post = postRps.findById(request.id())
                .orElseThrow(()-> new NoSuchElementException(null));
        System.out.println("post:"+post);
        post.setTitle(request.title());
        post.setContent(request.content());
        post.setStatus(request.status());
        post.setIsPublic(request.isPublic());

        post.setModifiedAt(new Date());
    }
}
