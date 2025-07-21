package com.icetea.MonStu.api.v2.controller;

import com.icetea.MonStu.api.v2.dto.MessageResponse;
import com.icetea.MonStu.api.v2.dto.request.CreatePostRequest;
import com.icetea.MonStu.api.v2.dto.request.FilterPostRequest;
import com.icetea.MonStu.api.v2.dto.response.CustomPageableResponse;
import com.icetea.MonStu.api.v2.dto.response.PostResponse;
import com.icetea.MonStu.api.v2.dto.response.PostSummaryResponse;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController("postControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/posts")
@Tag(name = "Post API", description = "게시물 관리")
public class PostController {

    private final PostService postSvc;

    @Operation(summary = "게시물 저장", description = "전달받은 게시물 정보 데이터베이스에 저장")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("")
    public ResponseEntity<PostResponse> savePost(@Valid @RequestBody CreatePostRequest createPostRequest, @AuthenticationPrincipal CustomUserDetails user){
        PostResponse postResponse = postSvc.create(createPostRequest,user.getId());
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
    public ResponseEntity<MessageResponse> deletePost(@PathVariable Long id ){
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
    @GetMapping("/me")
    public ResponseEntity<CustomPageableResponse<PostSummaryResponse>> getMyPosts(@AuthenticationPrincipal CustomUserDetails user, Pageable pageable ){
        Page<PostSummaryResponse> page = postSvc.getMyPosts(user.getId(),pageable);
        CustomPageableResponse<PostSummaryResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "공개 게시글 요약 데이터 조회", description = "is_public 속성이 'true'인 모든 게시글 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("")
    public ResponseEntity<CustomPageableResponse<PostSummaryResponse>> getPublicPosts( Pageable pageable ){
        Page<PostSummaryResponse> page = postSvc.getPublicPosts(pageable);
        CustomPageableResponse<PostSummaryResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "필터링된 게시물 데이터 목록 반환", description = "페이지 정보와 필터링 정보를 이용, 필터링된 게시물 데이터 목록 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "반환 실패")
    })
    @GetMapping("/filter")  //search로 변경
    public ResponseEntity<CustomPageableResponse<PostResponse>> getPostsWithfilter(@RequestBody FilterPostRequest postFilter, Pageable pageable) {
        Page<PostResponse> page = postSvc.filter(postFilter,pageable);
        CustomPageableResponse<PostResponse> result = CustomPageableResponse.mapper(page);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(result);
    }

}
