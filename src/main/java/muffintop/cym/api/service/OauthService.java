package muffintop.cym.api.service;

import muffintop.cym.api.component.WebClientUtil;
import muffintop.cym.api.service.dto.kakao.KakaoToken;
import muffintop.cym.api.service.dto.kakao.KakaoUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OauthService {

    @Value("${host.kakao-token}")
    private String kakaoTokenHost;

    @Value("${host.kakao-user}")
    private String kakaoUserHost;

    @Value("${kakao.redirect-uri}")
    private String redirectURI;

    @Value("${kakao.key}")
    private String key;


    private KakaoUser getUser(String accessToken) {
        WebClient webClient = WebClientUtil.getBaseUrl(kakaoUserHost);
        try {
            ResponseEntity<KakaoUser> response = webClient
                .post()
                .header("Authorization", "Bearer " + accessToken).retrieve()
                .toEntity(KakaoUser.class).block();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private KakaoToken getUserToken(String code) {
        WebClient webClient = WebClientUtil.getBaseUrl(kakaoTokenHost);
        try {
            ResponseEntity<KakaoToken> response = webClient.post().uri(uriBuilder ->
                uriBuilder
                    .queryParam("client_id", key)
                    .queryParam("redirect_uri", redirectURI)
                    .queryParam("code", code)
                    .queryParam("grant_type", "authorization_code")
                    .build()).retrieve().toEntity(KakaoToken.class).block();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public KakaoUser getKakaoUser(String code) {
        KakaoToken token = getUserToken(code);
        KakaoUser kakaoUser = getUser(token.getAccessToken());
        return kakaoUser;
    }

}
