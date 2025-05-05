package com.icetea.MonStu.service;

import com.icetea.MonStu.dto.common.PostDTO;
import com.icetea.MonStu.dto.request.PostRequest;
import com.icetea.MonStu.dto.response.PostLiteResponse;
import com.icetea.MonStu.entity.Member;
import com.icetea.MonStu.entity.Post;
import com.icetea.MonStu.enums.PostStatus;
import com.icetea.MonStu.repository.MemberRepository;
import com.icetea.MonStu.repository.PostRepository;
import com.icetea.MonStu.security.CustomUserDetails;
import com.querydsl.core.types.Predicate;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.NoSuchElementException;

import static com.icetea.MonStu.entity.QPost.post;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRps;
    private final MemberRepository memberRps;

    private final UserDetailsService userDetailsService;

    private final ModelMapper modelMapper;
    private final JPAQueryFactory queryFactory;

    @Transactional
    public PostDTO save(PostRequest postRequest) {
        Member member = memberRps.findByEmail( postRequest.authorEmail() )
                .orElseThrow(()->new NoSuchElementException("회원 정보를 찾을 수 없습니다"));
        Post post = Post.builder()
                .id( postRequest.id() )
                .title( postRequest.title() )
                .content( postRequest.content() )
                .createdAt( new Date())
                .status( PostStatus.PUBLIC )
                .isPublic( postRequest.isPublic() )
                .member( member )
                .build();
        Post savedPost  = postRps.save(post);
        PostDTO postDTO = modelMapper.map(savedPost, PostDTO.class);
        postDTO.setAuthorEmail( member.getEmail() );
        return postDTO;
    }

    public Page<PostLiteResponse> getFilteredPosts(CustomUserDetails user, Pageable pageable) {
        Predicate predicate = post.member.id.eq(user.getId());

        return postRps.findAll(predicate, pageable)
                .map(e -> new PostLiteResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getCreatedAt(),
                        e.getModifiedAt(),
                        e.getStatus(),
                        e.getIsPublic(),
                        e.getMember().getId(),
                        e.getMember().getNickName()
                ));
    }

    public Page<PostLiteResponse> getPublicPosts(Pageable pageable) {
        Predicate predicate = post.isPublic.eq(true);

        return postRps.findAll(predicate, pageable)
                .map(e -> new PostLiteResponse(
                        e.getId(),
                        e.getTitle(),
                        e.getCreatedAt(),
                        e.getModifiedAt(),
                        e.getStatus(),
                        e.getIsPublic(),
                        e.getMember().getId(),
                        e.getMember().getNickName()
                ));
    }

    @Transactional
    public void deleteBtId(Long postId) {
        if (!postRps.existsById(postId))  throw new NoSuchElementException("게시물을 찾을 수 없습니다");
        postRps.deleteById(postId);
    }

    public PostDTO findById(Long postId) {
        Post post = postRps.findById(postId).orElseThrow(NoSuchElementException::new);
        return modelMapper.map(post, PostDTO.class);
    }
}
