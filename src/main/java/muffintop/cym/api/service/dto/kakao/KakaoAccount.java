package muffintop.cym.api.service.dto.kakao;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class KakaoAccount {

    @JsonProperty("profile_nickname_needs_agreement")
    private boolean profileNicknameNeedsAgreement;

    @JsonProperty("profile_image_needs_agreement")
    private boolean profileImageNeedsAgreement;

    @JsonProperty("profile")
    private KakaoProfile kakaoProfile;

    @JsonProperty("email")
    private String email;

    @JsonProperty("has_email")
    private boolean hasEmail;

    @JsonProperty("email_needs_agreement")
    private boolean emailNeedsAgreement;

    @JsonProperty("is_email_valid")
    private boolean isEmailValid;

    @JsonProperty("is_email_verified")
    private boolean isEmailVerified;


}
