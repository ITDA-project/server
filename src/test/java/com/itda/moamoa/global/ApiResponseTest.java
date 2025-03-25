package com.itda.moamoa.global;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itda.moamoa.global.common.ApiResponse;
import com.itda.moamoa.global.common.SuccessCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ApiResponseTest {

    private static final Logger logger = LoggerFactory.getLogger(ApiResponseTest.class);
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("단일 데이터 성공 응답 생성 테스트")
    void successSingleDataTest() {
        // given
        String testMessage = "성공 메시지";
        String testData = "테스트 데이터";
        SuccessCode successCode = SuccessCode.OK; // 실제 코드에 맞게 수정 필요

        // when
        ApiResponse<String> response = ApiResponse.success(successCode, testMessage, testData);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo(successCode.getCode());
        assertThat(response.getMessage()).isEqualTo(testMessage);
        assertThat(response.getData()).isEqualTo(testData);
        assertThat(response.getDtoList()).isNull();
        assertThat(response.getSize()).isNull();
    }

    @Test
    @DisplayName("리스트 데이터 성공 응답 생성 테스트")
    void successListDataTest() {
        // given
        String testMessage = "목록 조회 성공";
        List<String> testList = Arrays.asList("항목1", "항목2", "항목3");
        Integer size = testList.size();
        SuccessCode successCode = SuccessCode.OK; // 실제 코드에 맞게 수정 필요

        // when
        ApiResponse<String> response = ApiResponse.successList(successCode, testMessage, testList, size);

        // then
        assertThat(response.getStatus()).isEqualTo("success");
        assertThat(response.getCode()).isEqualTo(successCode.getCode());
        assertThat(response.getMessage()).isEqualTo(testMessage);
        assertThat(response.getDtoList()).isEqualTo(testList);
        assertThat(response.getSize()).isEqualTo(size);
        assertThat(response.getData()).isNull();
    }

    @Test
    @DisplayName("JSON 변환 시 null 필드 제외 테스트")
    void jsonIncludeNonNullTest() throws Exception {
        // given
        String testMessage = "성공 메시지";
        String testData = "테스트 데이터";
        SuccessCode successCode = SuccessCode.OK; // 실제 코드에 맞게 수정 필요
        ApiResponse<String> response = ApiResponse.success(successCode, testMessage, testData);

        // when
        String json = objectMapper.writeValueAsString(response);

        // then
        assertThat(json).contains("status", "code", "message", "data");
        assertThat(json).doesNotContain("dtoList", "size");
    }

    @Test
    @DisplayName("DTO 모델 응답 테스트")
    void dtoModelResponseTest() {
        // given
        TestDto testDto = new TestDto(1L, "테스트 이름");
        String testMessage = "DTO 응답 성공";
        SuccessCode successCode = SuccessCode.OK; // 실제 코드에 맞게 수정 필요

        // when
        ApiResponse<TestDto> response = ApiResponse.success(successCode, testMessage, testDto);

        // then
        assertThat(response.getData()).isEqualTo(testDto);
        assertThat(response.getData().getId()).isEqualTo(1L);
        assertThat(response.getData().getName()).isEqualTo("테스트 이름");
    }

    @Test
    @DisplayName("리뷰 데이터를 포함한 API 응답의 JSON 형식 출력 테스트")
    void reviewResponseJsonTest() throws Exception {
        // given
        ReviewDto reviewDto = new ReviewDto(
                "1",
                "qwer1234",
                4,
                "괜찮았어요!",
                "2025-02-14T12:35:00Z"
        );
        
        SuccessCode successCode = SuccessCode.CREATED; // 201 코드 가정
        String message = "Review has been created successfully.";
        
        ApiResponse<ReviewDto> response = ApiResponse.success(successCode, message, reviewDto);
        
        // when
        String jsonResponse = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);
        
        // then
        logger.info("API Response JSON:\n{}", jsonResponse);
        System.out.println("API Response JSON:");
        System.out.println(jsonResponse);
    }

    // 테스트용 DTO 클래스
    static class TestDto {
        private Long id;
        private String name;

        public TestDto(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            TestDto testDto = (TestDto) o;
            return id.equals(testDto.id) && name.equals(testDto.name);
        }
    }

    static class ReviewDto {
        private String reviewID;
        private String userID;
        private int star;
        private String sentence;
        private String createdAt;

        public ReviewDto(String reviewID, String userID, int star, String sentence, String createdAt) {
            this.reviewID = reviewID;
            this.userID = userID;
            this.star = star;
            this.sentence = sentence;
            this.createdAt = createdAt;
        }

        public ReviewDto() {
        }

        public String getReviewID() { return reviewID; }
        public void setReviewID(String reviewID) { this.reviewID = reviewID; }

        public String getUserID() { return userID; }
        public void setUserID(String userID) { this.userID = userID; }

        public int getStar() { return star; }
        public void setStar(int star) { this.star = star; }

        public String getSentence() { return sentence; }
        public void setSentence(String sentence) { this.sentence = sentence; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}