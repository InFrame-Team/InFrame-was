package com.InFrame.common.exception;

import com.InFrame.common.exception.error.ErrorCode;
import com.InFrame.common.exception.error.ErrorDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 커스텀 예외처리
    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<?> customExceptionHandler(CustomException e) {
        ErrorCode errorCode = e.getErrorCode();
        ErrorDto errorDto = new ErrorDto(errorCode.getStatus(), errorCode.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatusCode.valueOf(errorCode.getStatus()));
    }

    // 일반 예외처리
    @ExceptionHandler
    protected ResponseEntity<?> customServerException(Exception e){
        log.error("INTERNAL_SERVER_ERROR", e);
        ErrorDto errorDto = new ErrorDto(ErrorCode.INTERNAL_SERVER_ERROR.getStatus(), ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
        return new ResponseEntity<>(errorDto, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // 메소드 인자 타당성 예외 처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        for(FieldError error : e.getBindingResult().getFieldErrors())
            errors.put(error.getField(), error.getDefaultMessage());
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
