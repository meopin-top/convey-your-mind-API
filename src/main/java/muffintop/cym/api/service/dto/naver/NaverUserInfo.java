package muffintop.cym.api.service.dto.naver;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class NaverUserInfo {

    @JsonProperty("id")
    private String id;

    @JsonProperty("nickname")
    private String nickName;

    @JsonProperty("profile_image")
    private String profileImage;

    @JsonProperty("email")
    private String email;
}
