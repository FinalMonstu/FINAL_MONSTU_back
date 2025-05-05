package com.icetea.MonStu.controller;

import com.icetea.MonStu.dto.common.PostDTO;
import com.icetea.MonStu.dto.request.PostRequest;
import com.icetea.MonStu.dto.response.CustomPageableResponse;
import com.icetea.MonStu.dto.response.PostLiteResponse;
import com.icetea.MonStu.security.CustomUserDetails;
import com.icetea.MonStu.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "게시물 관리")
public class PostController {

    private final PostService postSvc;

    @Operation(summary = "게시물 저장", description = "전달받은 게시물 정보를 데이터베이스에 저장합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "저장 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @PostMapping("/save")
    public ResponseEntity<PostDTO> savePost(@RequestBody PostRequest postRequest){
        PostDTO postDto = postSvc.save(postRequest);
        return  new ResponseEntity<>(postDto, HttpStatus.OK);
    }


    @Operation(summary = "게시물 삭제", description = "게시물 ID를 이용하여 데이터 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @DeleteMapping("/{postId}")
    public ResponseEntity<?> deletePostById( @PathVariable Long postId ){
        postSvc.deleteBtId(postId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @Operation(summary = "게시글 조회", description = "게시글 ID를 이용하여 게시물 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{postId}")
    public ResponseEntity<?> getPostById( @PathVariable Long postId ){
        PostDTO postDTO = postSvc.findById(postId);
        return new ResponseEntity<>(postDTO,HttpStatus.OK);
    }


    @Operation(summary = "로그인한 사용자의 게시물 목록 반환", description = "회원의 게시물 목록을 반환합니다")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "반환 성공"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/posts/me")
    public ResponseEntity<CustomPageableResponse> getPostsById( @AuthenticationPrincipal CustomUserDetails user, Pageable pageable ){
        Page<PostLiteResponse> page = postSvc.getFilteredPosts(user,pageable);
        CustomPageableResponse<PostLiteResponse> result = CustomPageableResponse.from(page);
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Operation(summary = "공개 게시글 조회", description = "is_public 값이 true인 게시글을 모두 반환")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/posts")
    public ResponseEntity<?> getPublicPosts( Pageable pageable ){
        Page<PostLiteResponse> page = postSvc.getPublicPosts(pageable);
        CustomPageableResponse<PostLiteResponse> result = CustomPageableResponse.from(page);
        return  new ResponseEntity<>(result, HttpStatus.OK);
    }
}
