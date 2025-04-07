package com.itda.moamoa.global;

import com.itda.moamoa.global.common.ErrorCode;
import com.itda.moamoa.global.common.ErrorResponse;
import com.itda.moamoa.global.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    public void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void methodArgumentNotValidException_테스트() {
        // given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("objectName", "field", "기본 메시지");

        when(exception.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Collections.singletonList(fieldError));

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodArgumentNotValidException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.INVALID_INPUT_VALUE.getCode(), response.getBody().getCode());
    }

    @Test
    public void missingServletRequestParameterException_테스트() {
        // given
        MissingServletRequestParameterException exception = new MissingServletRequestParameterException("paramName", "paramType");

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMissingServletRequestParameterException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.BAD_REQUEST.getCode(), response.getBody().getCode());
    }

    @Test
    public void methodArgumentTypeMismatchException_테스트() {
        // given
        MethodArgumentTypeMismatchException exception = mock(MethodArgumentTypeMismatchException.class);

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleMethodArgumentTypeMismatchException(exception);

        // then
        assertEquals(HttpStatus.BAD_REQUEST.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.INVALID_TYPE_VALUE.getCode(), response.getBody().getCode());
    }

    @Test
    public void accessDeniedException_테스트() {
        // given
        AccessDeniedException exception = new AccessDeniedException("접근 거부됨");

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAccessDeniedException(exception);

        // then
        assertEquals(HttpStatus.FORBIDDEN.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.FORBIDDEN.getCode(), response.getBody().getCode());
    }

    @Test
    public void authenticationException_테스트() {
        // given
        AuthenticationException exception = mock(AuthenticationException.class);

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleAuthenticationException(exception);

        // then
        assertEquals(HttpStatus.UNAUTHORIZED.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.UNAUTHORIZED.getCode(), response.getBody().getCode());
    }

    @Test
    public void exception_테스트() {
        // given
        Exception exception = new Exception("일반 예외 발생");

        // when
        ResponseEntity<ErrorResponse> response = exceptionHandler.handleException(exception);

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCode().value());
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR.getCode(), response.getBody().getCode());
    }
}