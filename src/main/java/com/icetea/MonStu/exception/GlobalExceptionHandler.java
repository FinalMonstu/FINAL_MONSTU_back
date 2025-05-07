package com.icetea.MonStu.exception;

import com.icetea.MonStu.dto.response.ErrorResponse;
import com.icetea.MonStu.dto.response.MessageResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;


@ControllerAdvice
public class GlobalExceptionHandler {


    // 모든 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("Internal_Server_Error",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "잠시 후 다시 시도해주세요."));
    }

    // 400 Bad Request - 잘못된 요청
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("BAD_REQUEST",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "잘못된 요청입니다. 요청을 다시 확인해 주세요."));
    }


    // 401 Unauthorized - 인증 실패
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<?> handleUnauthorizedException(UnauthorizedException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse( "Unauthorized", StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "로그인이 필요합니다.") );
    }

    // 403 Forbidden - 권한 없음
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<?> handleForbiddenException(ForbiddenException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("Forbidden",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "이 리소스에 접근할 권한이 없습니다."));
    }

    // 404 Not Found
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<?> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("NoSuch Data", StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "데이터가 존재하지 않습니다") );
    }

    // 409 Conflict - 리소스 충돌
    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<?> handleConflictException(ConflictException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("Conflict", StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "이미 존재하는 리소스입니다."));
    }

    // 500
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity
                .badRequest()
                .body(new ErrorResponse("BAD_REQUEST",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "입력을 다시 확인해주세요."));
    }

    // AuthenticationException - 401 Unauthorized
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthException(AuthenticationException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("Unauthorized",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "일치하는 정보가 없습니다"));
    }

}

