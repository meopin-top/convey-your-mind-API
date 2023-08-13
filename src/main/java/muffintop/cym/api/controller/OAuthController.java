package muffintop.cym.api.controller;


import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.JwtTokenManager;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.domain.Token;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.service.OauthService;
import muffintop.cym.api.service.UserService;
import muffintop.cym.api.service.dto.kakao.KakaoUser;
import muffintop.cym.api.service.dto.naver.NaverUser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("api/oauth")
@RestController
public class OAuthController {

    private final OauthService oauthService;
    private final UserService userService;
    private final JwtTokenManager tokenManager;

    @GetMapping("/kakao/callback")
    public ResponseEntity<CommonResponse> kakaoLogin(@RequestParam String code,
        HttpServletResponse response) {
        KakaoUser kakaoUser = oauthService.getKakaoUser(code);
        User user = userService.signInWithKakao(kakaoUser);
        Token token = tokenManager.generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        response.addCookie(accessTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK, user);
    }

    @GetMapping("/naver/callback")
    public ResponseEntity<CommonResponse> naverLogin(@RequestParam String code,
        HttpServletResponse response) {
        NaverUser naverUser = oauthService.getNaverUser(code);
        User user = userService.signInWithNaver(naverUser);
        Token token = tokenManager.generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        response.addCookie(accessTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK, user);
    }
}
