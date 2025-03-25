package com.itda.moamoa.global;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class ErrorResponseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(ErrorResponseTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();
    
    @Test
    @DisplayName("유효성 검증 오류 응답의 JSON 형식 출력 테스트")
    void validationErrorResponseJsonTest() throws Exception {
        // given
        List<FieldError> errors = Arrays.asList(
            new FieldError("userID", "User ID is required."),
            new FieldError("star", "Star rating must be between 1 and 5."),
            new FieldError("sentence", "Sentence is required.")
        );
        
        ErrorResponse errorResponse = new ErrorResponse("error", 400, "Invalid request parameters.", errors);
        
        // when
        String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(errorResponse);
        
        // then
        logger.info("Error Response JSON:\n{}", jsonResponse);
        System.out.println("Error Response JSON:");
        System.out.println(jsonResponse);
    }
    
    // ErrorResponse 클래스
    public static class ErrorResponse {
        @JsonProperty("status")
        private String status;
        
        @JsonProperty("code")
        private int code;
        
        @JsonProperty("message")
        private String message;
        
        @JsonProperty("errors")
        private List<FieldError> errors;
        
        public ErrorResponse() {
        }
        
        public ErrorResponse(String status, int code, String message, List<FieldError> errors) {
            this.status = status;
            this.code = code;
            this.message = message;
            this.errors = errors;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public int getCode() {
            return code;
        }
        
        public void setCode(int code) {
            this.code = code;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public List<FieldError> getErrors() {
            return errors;
        }
        
        public void setErrors(List<FieldError> errors) {
            this.errors = errors;
        }
    }
    
    // 필드 오류 클래스
    public static class FieldError {
        @JsonProperty("field")
        private String field;
        
        @JsonProperty("message")
        private String message;
        
        public FieldError() {
        }
        
        public FieldError(String field, String message) {
            this.field = field;
            this.message = message;
        }
        
        public String getField() {
            return field;
        }
        
        public void setField(String field) {
            this.field = field;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}
