package muffintop.cym.api.controller;

import javax.mail.MessagingException;
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
import muffintop.cym.api.interceptor.Auth;
import muffintop.cym.api.repository.UserResolver;
import muffintop.cym.api.service.EmailService;
import muffintop.cym.api.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest request, HttpServletResponse response)
        throws MessagingException {
        User user = userService.signUp(request);
        if (user.getEmail() != null) {
            emailService.sendMail(user.getEmail());
        }
        Token token = tokenManager.generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_UP_SUCCESS, HttpStatus.OK, user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest request,
        HttpServletResponse response) {
        User user = userService.signIn(request);
        Token token = tokenManager.generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK, user);
    }

    @Auth
    @GetMapping()
    public ResponseEntity<CommonResponse> getUserInfo(@UserResolver User user) {
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK, user);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("AccessToken", null);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return ResponseHandler.generateResponse(ResponseCode.LOGOUT_SUCCESS, HttpStatus.OK, null);
    }


}
