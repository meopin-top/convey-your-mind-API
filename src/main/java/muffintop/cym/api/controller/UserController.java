package muffintop.cym.api.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.JwtTokenManager;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.request.SignInRequest;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.controller.response.CommonResponse;
import muffintop.cym.api.domain.Token;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenManager tokenManager;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest request){
        User user = userService.signUp(request);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_UP_SUCCESS, HttpStatus.OK,null);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest request,
        HttpServletResponse response){
        User user = userService.signIn(request);
        Token token = tokenManager.generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        Cookie refreshTokenCookie = new Cookie("RefreshToken", token.getRefreshToken());
        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK,user);
    }

}
