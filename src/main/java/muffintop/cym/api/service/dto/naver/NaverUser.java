package muffintop.cym.api.service.dto.naver;

import lombok.Data;

@Data
public class NaverUser {

    private String resultCode;

    private String message;

    private NaverUserInfo response;

}
