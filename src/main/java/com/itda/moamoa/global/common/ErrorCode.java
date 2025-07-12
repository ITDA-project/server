package com.itda.moamoa.global.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 입력값입니다."),
    INVALID_TYPE_VALUE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 타입입니다."),
    ENTITY_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, "요청한 리소스를 찾을 수 없습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),

    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "로그인이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, 404, "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "지원하지 않는 HTTP 메소드입니다."),
    CONFLICT(HttpStatus.CONFLICT, 409, "이미 존재하는 데이터입니다."),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류가 발생했습니다."),

    //마이페이지 구현하며 추가 - 사용자 관련 오류
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 404, "사용자를 찾을 수 없습니다."),
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "파일 업로드에 실패했습니다."),
    
    // 리뷰 관련 에러 코드
    CANNOT_REVIEW_SELF(HttpStatus.BAD_REQUEST, 400, "자신의 리뷰를 작성할 수 없습니다."),
    NOT_PARTICIPATED_TOGETHER(HttpStatus.BAD_REQUEST, 400, "함께 참여하지 않은 활동에 대한 리뷰를 작성할 수 없습니다."),

    // 알림 관련 에러 코드
    NOTIFICATION_NOT_FOUND(HttpStatus.BAD_REQUEST, 400, "사용자에게 수신된 알림이 존재하지 않습니다.");

    private final HttpStatus status;
    private final int code;
    private final String message;
}