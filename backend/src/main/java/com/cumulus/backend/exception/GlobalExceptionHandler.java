package com.cumulus.backend.exception;

import com.cumulus.backend.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException customException){
        return ResponseEntity
                .status(customException.getErrorCode().getStatus())
                .body(ApiResponse.fail(null, customException.getErrorCode().name(),
                        customException.getErrorCode().getMessage() ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleOther(Exception exception){
        log.error("[전역예외] 처리되지 않은 예외발생", exception);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(ApiResponse.fail(null, "INTERNAL_SERVER_ERROR", "서버 내부 오류"));
    }
}
