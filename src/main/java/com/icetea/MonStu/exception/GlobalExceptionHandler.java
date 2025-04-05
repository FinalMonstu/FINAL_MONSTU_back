package com.icetea.MonStu.exception;

import com.icetea.MonStu.dto.response.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {


    // 모든 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Internal_Server_Error","잠시 후 다시 시도해주세요"));
    }

    // 400 Bad Request - 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException e) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("BAD_REQUEST","잘못된 요청입니다. 요청을 다시 확인해 주세요."));
    }

    // 500
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("BAD_REQUEST",ex.getMessage()));
    }

    // 401 Unauthorized - 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException e) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Unauthorized","로그인이 필요합니다."));
    }

    // 403 Forbidden - 권한 없음
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException e) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Forbidden","이 리소스에 접근할 권한이 없습니다."));
    }

    // 409 Conflict - 리소스 충돌
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException e) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Conflict", "이미 존재하는 리소스입니다."));
    }
}

