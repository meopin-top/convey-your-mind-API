package muffintop.cym.api.controller.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {

    //USER
    // 회원가입
    SIGN_UP_SUCCESS(1001, "회원가입이 완료되었습니다"),
    DUPLICATED_ID(1011, "중복된 ID 입니다."),
    INCORRECT_PASSWORD(1012, "패스워드가 일치하지 않습니다."),
    INVALID_FORMAT_PASSWORD(1013, "패스워드의 형태가 유효하지 않습니다"),

    // 로그인
    SIGN_IN_SUCCESS(1101, "로그인이 완료되었습니다."),
    NON_EXIST_ID(1111, "존재하지 않는 아이디입니다."),
    INVALID_PASSWORD(1112, "패스워드가 유효하지 않습니다."),

    // 파일
    FILE_UPLOAD_SUCCESS(2001, "파일이 업로드 되었습니다."),
    FILE_DELETE_SUCCESS(2002, "파일이 삭제되었습니다."),
    FILE_UPLOAD_FAIL(2011, "파일 업로드가 실패하였습니다."),
    FILE_DELETE_FAIL(2012, "파일 삭제가 실패하였습니다.");

    private int code;
    private String message;


}
