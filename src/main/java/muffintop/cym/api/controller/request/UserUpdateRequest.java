package muffintop.cym.api.controller.request;

import lombok.Data;

@Data
public class UserUpdateRequest {

    private String password;

    private String passwordCheck;

    private String nickname;

    private String email;

    private String profileUri;

}
