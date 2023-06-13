package muffintop.cym.api.controller.request;

import lombok.Data;

@Data
public class SignUpRequest {

    private String userId;

    private String email;

    private String password;

    private String passwordCheck;
}
