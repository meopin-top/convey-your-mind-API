package muffintop.cym.api.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class KakaoUser {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("connected_at")
    private LocalDateTime connectedAt;

    @JsonProperty("properties")
    private KakaoProperty kakaoProperty;

    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

}
