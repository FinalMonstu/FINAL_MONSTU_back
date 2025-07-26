package com.icetea.MonStu.exception;

import com.icetea.MonStu.api.v2.dto.response.ErrorResponse;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.rmi.ServerError;


@RestControllerAdvice
public class GlobalExceptionHandler {

    // @Vaild 실패 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String msg = ex.getBindingResult().getFieldErrors().getFirst().getDefaultMessage();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("VALIDATION_ERROR", msg));
    }

    // 모든 예외 처리 (500 Internal Server Error)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("INTERNAL_SERVER_ERROR",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "잠시 후 다시 시도해주세요."));
    }

    // 400 Bad Request - 잘못된 요청
    @ExceptionHandler(EmptyParameterException.class)
    public ResponseEntity<?> handleBadRequestException(EmptyParameterException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("BAD_REQUEST",StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "요청이 비어있습니다."));
    }

    // 400 Bad Request - 빈 값을 전달
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<?> handleBadRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
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
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("NOT_FOUND", StringUtils.hasText(ex.getMessage()) ? ex.getMessage() :  "데이터가 존재하지 않습니다") );
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

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("UNAUTHORIZED", "정보가 일치하지 않습니다"));
    }



    // Google Translate API 토큰 모두 소진
    @ExceptionHandler(GoogleResourceExhaustedException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentials(GoogleResourceExhaustedException ex) {
        return ResponseEntity
                .status(HttpStatus.TOO_MANY_REQUESTS)
                .body(new ErrorResponse("TOO_MANY_REQUESTS", "번역 토큰이 모두 소진되었습니다. 내일 다시 시도해주세요."));
    }

}

