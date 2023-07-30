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

    // 로그인
    SIGN_IN_SUCCESS(1101, "로그인이 완료되었습니다."),
    NON_EXIST_ID(1111, "존재하지 않는 ID 입니다. 다시 한번 확인해 주세요."),
    INVALID_PASSWORD(1112, "PW를 잘못 입력하셨습니다. 다시 한 번 확인해주세요."),

    // 파일
    FILE_UPLOAD_SUCCESS(2001, "파일이 업로드 되었습니다."),
    FILE_DELETE_SUCCESS(2002, "파일이 삭제되었습니다."),
    FILE_UPLOAD_FAIL(2011, "파일 업로드가 실패하였습니다."),
    FILE_DELETE_FAIL(2012, "파일 삭제가 실패하였습니다.");

    private int code;
    private String message;


}
