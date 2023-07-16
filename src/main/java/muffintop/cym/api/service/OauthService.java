package muffintop.cym.api.service;

import muffintop.cym.api.component.WebClientUtil;
import muffintop.cym.api.service.dto.kakao.KakaoToken;
import muffintop.cym.api.service.dto.kakao.KakaoUser;
import muffintop.cym.api.service.dto.naver.NaverToken;
import muffintop.cym.api.service.dto.naver.NaverUser;
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
    private String kakaoRedirectURI;

    @Value("${kakao.key}")
    private String kakaoKey;

    @Value("${host.naver-token}")
    private String naverTokenHost;

    @Value("${host.naver-user}")
    private String naverUserHost;

    @Value("${naver.redirect-uri}")
    private String naverRedirectURI;

    @Value("${naver.key}")
    private String naverKey;

    @Value("${naver.secret}")
    private String naverSecret;

    @Value("${naver.state}")
    private String naverState;


    private KakaoUser getKakaoResponse(String accessToken) {
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

    private KakaoToken getKaKaoUserToken(String code) {
        WebClient webClient = WebClientUtil.getBaseUrl(kakaoTokenHost);
        try {
            ResponseEntity<KakaoToken> response = webClient.post().uri(uriBuilder ->
                uriBuilder
                    .queryParam("client_id", kakaoKey)
                    .queryParam("redirect_uri", kakaoRedirectURI)
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
        KakaoToken token = getKaKaoUserToken(code);
        KakaoUser kakaoUser = getKakaoResponse(token.getAccessToken());
        return kakaoUser;
    }

    private NaverUser getNaverResponse(String accessToken) {
        WebClient webClient = WebClientUtil.getBaseUrl(naverUserHost);
        try {
            ResponseEntity<NaverUser> response = webClient
                .post()
                .header("Authorization", "Bearer " + accessToken).retrieve()
                .toEntity(NaverUser.class).block();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private NaverToken getNaverUserToken(String code) {
        WebClient webClient = WebClientUtil.getBaseUrl(naverTokenHost);
        try {
            ResponseEntity<NaverToken> response = webClient.post().uri(uriBuilder ->
                uriBuilder
                    .queryParam("client_id", naverKey)
                    .queryParam("client_secret", naverSecret)
                    .queryParam("state", naverState)
                    .queryParam("code", code)
                    .queryParam("grant_type", "authorization_code")
                    .build()).retrieve().toEntity(NaverToken.class).block();
            return response.getBody();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public NaverUser getNaverUser(String code) {
        NaverToken token = getNaverUserToken(code);
        NaverUser naverUser = getNaverResponse(token.getAccessToken());
        return naverUser;
    }

}
