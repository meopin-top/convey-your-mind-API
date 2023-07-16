package muffintop.cym.api.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoProperty {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("thumbnail_image")
    private String thumbnailImage;

    @JsonProperty("profile_image")
    private String profileImage;

}
