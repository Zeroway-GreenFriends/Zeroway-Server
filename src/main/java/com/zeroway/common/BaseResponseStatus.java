package com.zeroway.common;

import lombok.Getter;
import lombok.ToString;

/**
 * 에러 코드 관리
 */
@Getter
@ToString
public enum BaseResponseStatus {
    /**
     * 1000 : 요청 성공
     */
    SUCCESS(true, 1000, "요청에 성공하였습니다."),


    /**
     * 2000 : Request 오류
     */
    // Common
    REQUEST_ERROR(false, 2000, "입력값을 확인해주세요."),
    
    EMPTY_JWT(false, 2001, "JWT를 입력해주세요."),
    INVALID_JWT(false, 2002, "유효하지 않은 JWT입니다."),
    POST_USER_EMPTY_EMAIL(false, 2003, "이메일 값을 넣어주세요."),
    POST_USER_INVALID_EMAIL(false, 2004, "이메일 형식을 확인해주세요."),
    LOGIN_FAILED(false, 2005, "존재하지 않는 회원입니다."),
    POST_USER_INACTIVE(false, 2006, "탈퇴 처리된 회원입니다."),
    POST_USERS_EXISTS_EMAIL(false, 2021, "이미 가입된 이메일입니다."),

    INVALID_POST_ID(false, 2100, "유효하지 않은 게시글 식별자입니다."),
    INVALID_PARAMETER_VALUE(false, 2101, "잘못된 요청 파라미터 값입니다."),
    INVALID_COMMENT_ID(false, 2102, "유효하지 않은 댓글 식별자입니다."),

    INVALID_STORE_ID(false, 2200, "잘못된 제로웨이스트샵 식별자입니다."),

    INVALID_USER_ID(false, 2300, "잘못된 회원 식별자입니다."),

    UNAUTHORIZED_REQUEST(false, 2301, "권한이 없습니다."),

    // CS
    INVALID_QNA_ID(false, 2400, "잘못된 문의 내역 식별자입니다."),
    INVALID_ANNOUNCE_ID(false, 2401, "잘못된 공지 사항 식별자입니다."),
    INVALID_FAQ_ID(false, 2402, "잘못된 자주 묻는 질문 식별자입니다."),
    INVALID_QUESION_TYPE(false, 2403, "잘못된 질문 유형입니다."),
    INVALID_REPORT_TYPE(false, 2404, "잘못된 신고 유형입니다."),

    INVALID_LEVEL_ID(false, 2600, "존재하지 않는 레벨입니다."),

    // 커뮤니티
    TOO_MANY_IMAGES(false, 2700, "이미지는 최대 6장까지만 업로드 가능합니다."),


    /**
     * 3000 : Response 오류
     */
    // Common
    RESPONSE_ERROR(false, 3000, "값을 불러오는데 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, 3001, "비밀번호 암호화에 실패하였습니다."),
    EXPIRATION_JWT(false, 3002, "토큰이 만료되었습니다."),
    ALREADY_DELETED(false, 3100, "이미 삭제되었습니다."),

    ALREADY_WRITTEN(false, 3200, "이미 작성한 리뷰가 있습니다."),

    /**
     * 4000 : Database, Server 오류
     */
    DATABASE_ERROR(false, 4000, "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, 4001, "서버와의 연결에 실패하였습니다."),
    FILE_UPLOAD_ERROR(false, 4100, "파일 업로드에 실패하였습니다.");


    // 5000 : 필요시 만들어서 쓰세요
    // 6000 : 필요시 만들어서 쓰세요


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
