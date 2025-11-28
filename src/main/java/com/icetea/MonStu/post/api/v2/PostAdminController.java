package com.icetea.MonStu.post.api.v2;

import com.icetea.MonStu.post.application.PostService;
import com.icetea.MonStu.post.dto.v2.request.UpdatePostRequest;
import com.icetea.MonStu.post.dto.v2.response.PostResponse;
import com.icetea.MonStu.shared.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController("postAdminControllerV2")
@RequiredArgsConstructor
@RequestMapping("/api/v2/admin/posts")
@Tag(name = "Admin Post API", description = "관리자용 게시글 관리 API")
public class PostAdminController {

    private final PostService postSvc;


    @Operation(summary = "게시글과 로그 조회", description = "게시글 ID를 이용, 게시물 & 로그 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "404", description = "일치하는 게시물 없음"),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류")
    })
    @GetMapping("/{postsId}/logs")
    public ResponseEntity<PostResponse> findWithPostsAndLog(@PathVariable Long postsId ){
        PostResponse result = postSvc.findPostWithLogById(postsId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( result );
    }


    @Operation(summary = "여러 게시물 데이터 삭제", description = "전달받은 ID 목록을 이용, 해당 게시물 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "500", description = "서버 오류 실패")
    })
    @DeleteMapping("")
    public ResponseEntity<MessageResponse> deletePosts(@RequestParam("ids") List<Long> ids) {
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
    @PatchMapping("/{id}")
    public ResponseEntity<MessageResponse> updatePost(@Valid @RequestBody UpdatePostRequest updatePostRequest) {
        postSvc.updatePost(updatePostRequest);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body( new MessageResponse("수정 성공") );
    }
}
