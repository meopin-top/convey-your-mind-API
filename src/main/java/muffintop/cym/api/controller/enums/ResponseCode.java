package muffintop.cym.api.controller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    //USER

    UNAUTHORIZED(0, "접근 권한이 없습니다"),
    // 회원가입
    SIGN_UP_SUCCESS(1001, "회원가입이 완료되었습니다."),
    DUPLICATED_ID(1011, "이미 존재하는 ID 입니다."),
    INCORRECT_PASSWORD(1012, "패스워드가 일치하지 않습니다."),
    INVALID_FORMAT_PASSWORD(1013, "패스워드의 형태가 유효하지 않습니다."),
    INVALID_FORMAT_ID(1014, "ID의 형태가 유효하지 않습니다."),
    INVALID_FORMAT_EMAIL(1015, "EMAIL의 형태가 유효하지 않습니다."),
    EXISTED_EMAIL(1016, "이미 존재하는 이메일입니다."),

    // 로그인
    SIGN_IN_SUCCESS(1101, "로그인이 완료되었습니다."),
    NON_EXIST_ID(1111, "존재하지 않는 ID 입니다. 다시 한번 확인해 주세요."),
    INVALID_PASSWORD(1112, "PW를 잘못 입력하셨습니다. 다시 한 번 확인해주세요."),

    //로그아웃
    LOGOUT_SUCCESS(1201, "로그아웃이 완료되었습니다."),


    //유저정보 갱신
    USER_UPDATE_SUCCESS(1301, "변경이 완료되었습니다."),
    NICkNAME_GENERATE_SUCCESS(1302, "닉네임이 생성되었습니다."),
    MAIL_SEND_SUCCESS(1303, "메일이 전송되었습니다."),
    NO_EMAIL(1311, "이메일이 존재하지 않습니다."),

    // 파일
    FILE_UPLOAD_SUCCESS(2001, "파일이 업로드 되었습니다."),
    FILE_DELETE_SUCCESS(2002, "파일이 삭제되었습니다."),
    FILE_UPLOAD_FAIL(2011, "파일 업로드가 실패하였습니다."),
    FILE_DELETE_FAIL(2012, "파일 삭제가 실패하였습니다."),

    // 프로젝트
    PROJECT_READ_SUCCESS(3001, "프로젝트가 정상 조회되었습니다."),
    PROJECT_CREATE_SUCCESS(3002, "프로젝트가 정상 생성되었습니다."),
    PROJECT_UPDATE_SUCCESS(3003, "프로젝트가 정상 갱신되었습니다."),
    PROJECT_DELETE_SUCCESS(3004, "프로젝트가 정상 삭제되었습니다."),
    PROJECT_REGISTER_SUCCESS(3005, "프로젝트가 정상 등록되었습니다."),

    PROJECT_READ_FAIL(3011, "프로젝트 조회에 실패하였습니다."),
    PROJECT_CREATE_FAIL(3012, "프로젝트 생성에 실패하였습니다."),
    PROJECT_UPDATE_FAIL(3013, "프로젝트 갱신에 실패하였습니다."),
    PROJECT_DELETE_FAIL(3014, "프로젝트 삭제에 실패하였습니다."),

    PROJECT_INVITE_CODE_SUCCESS(3101, "초대코드가 생성되었습니다."),
    PROJECT_INVITE_CODE_EXISTED(3102, "초대코드가 존재합니다."),
    INVALID_INVITE_CODE(3111, "초대코드가 유효하지않습니다."),

    PROJECT_SUBMIT_SUCCESS(3201, "프로젝트가 제출되었습니다."),

    // 프로젝트 컨텐츠
    PROJECT_CONTENT_CREATE_SUCCESS(4001,"컨텐츠가 정상 생성되었습니다.");
    private int code;
    private String message;


}
