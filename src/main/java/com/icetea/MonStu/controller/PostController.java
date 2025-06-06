package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.request.post.PostFilterRequest;
import com.icetea.MonStu.dto.request.post.PostRequest;
import com.icetea.MonStu.dto.request.post.UpdatePostRequest;
import com.icetea.MonStu.dto.response.CustomPageableResponse;
import com.icetea.MonStu.dto.response.MessageResponse;
import com.icetea.MonStu.dto.response.post.PostLiteResponse;
import com.icetea.MonStu.dto.response.post.PostResponse;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "게시물 관리")
public class PostController {

    private final PostService postSvc;

    @Operation(summary = "게시물 저장", description = "전달받은 게시물 정보 데이터베이스에 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/save")
    public ResponseEntity<PostResponse> savePost(@Valid @RequestBody PostRequest request, @AuthenticationPrincipal CustomUserDetails user){
        PostResponse postResponse = postSvc.create(request,user.getId());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postResponse);
    }


    @Operation(summary = "게시물 삭제", description = "게시물 ID 이용, 데이터 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponse> deletePost( @PathVariable Long id ){
        postSvc.deleteById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new MessageResponse("삭제 성공"));
    }


    @Operation(summary = "게시글 조회", description = "게시글 ID를 이용하여 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id ){
        PostResponse postResponse = postSvc.getById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(postResponse);
    }


    @Operation(summary = "로그인한 사용자의 게시물 목록 반환", description = "회원의 게시물 목록을 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/me/posts")
    public ResponseEntity<CustomPageableResponse<PostLiteResponse>> getMyPosts( @AuthenticationPrincipal CustomUserDetails user, Pageable pageable ){
        Page<PostLiteResponse> page = postSvc.getMyPosts(user.getId(),pageable);
        CustomPageableResponse<PostLiteResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "공개 게시글 조회", description = "is_public 속성이 'true'인 모든 게시글 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/posts")
    public ResponseEntity<CustomPageableResponse<PostLiteResponse>> getPublicPosts( Pageable pageable ){
        System.out.println("pageable: "+pageable);
        Page<PostLiteResponse> page = postSvc.getPublicPosts(pageable);
        CustomPageableResponse<PostLiteResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "필터링된 게시물 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용, 필터링된 게시물 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @PostMapping("/filter")
    public ResponseEntity<CustomPageableResponse<com.icetea.MonStu.dto.response.post.PostResponse>> getPostsWithfilter(@RequestBody PostFilterRequest filter, Pageable pageable) {
        Page<com.icetea.MonStu.dto.response.post.PostResponse> page = postSvc.filter(filter,pageable);
        CustomPageableResponse<com.icetea.MonStu.dto.response.post.PostResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

    @Operation(summary = "게시글과 로그 조회", description = "게시글 ID를 이용하여 게시물 & 로그 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/detail/{id}")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<com.icetea.MonStu.dto.response.post.PostResponse> findWithPostsAndLog(@PathVariable Long id ){
        com.icetea.MonStu.dto.response.post.PostResponse result = postSvc.findWithMemberAndLogById(id);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }

    @Operation(summary = "여러 게시물 데이터 삭제", description = "전달받은 ID 목록을 이용, 해당 게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 실패")
    })
    @PostMapping("/delete")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<MessageResponse> deletePosts(@RequestBody List<Long> ids) {
        postSvc.deletePosts(ids);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("삭제 성공") );
    }


    @Operation(summary = "게시물 데이터 수정", description = "")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "수정 성공"),
            @ApiResponse(responseCode = "500", description = "수정_서버 오류 실패")
    })
    @PutMapping("/update")
    @PreAuthorize("hasRole(T(com.icetea.MonStu.enums.MemberRole).ADMIN.name())")
    public ResponseEntity<?> updatePost(@Valid @RequestBody UpdatePostRequest request) {
        postSvc.update(request);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }
}
