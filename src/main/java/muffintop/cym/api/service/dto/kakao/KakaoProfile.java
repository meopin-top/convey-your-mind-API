package muffintop.cym.api.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoProfile {

    @JsonProperty("nickname")
    private String nickname;

    @JsonProperty("thumbnail_image_url")
    private String thumbnailImageUrl;

    @JsonProperty("profile_image_url")
    private String profileImageUrl;

    @JsonProperty("is_default_image")
    private boolean isDefaultImage;

}
