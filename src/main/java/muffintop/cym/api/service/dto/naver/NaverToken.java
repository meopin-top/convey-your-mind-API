package muffintop.cym.api.service.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverToken {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("token_type")
    private String tokenType;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private Integer expiresIn;

}
