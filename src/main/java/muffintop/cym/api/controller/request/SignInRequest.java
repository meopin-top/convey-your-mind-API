package muffintop.cym.api.controller.request;

import lombok.Data;

@Data
public class SignInRequest {

    private String userId;

    private String password;

}
