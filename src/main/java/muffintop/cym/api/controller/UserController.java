package muffintop.cym.api.controller;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.JwtTokenManager;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.enums.ResponseCode;
import muffintop.cym.api.controller.request.SignInRequest;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.controller.request.UserUpdateRequest;
import muffintop.cym.api.controller.response.CommonResponse;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenManager tokenManager;
    private final EmailService emailService;

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest request,
        HttpServletResponse response)
        throws MessagingException {
        User user = userService.signUp(request);
        if (user.getEmail() != null) {
            emailService.sendWelcomeMail(user.getEmail(), user);
        }
        response.addCookie(tokenManager.makeCookie(user));
        return ResponseHandler.generateResponse(ResponseCode.SIGN_UP_SUCCESS, HttpStatus.OK, user);
    }

    @PostMapping("/sign-in")
    public ResponseEntity<CommonResponse> signIn(@RequestBody SignInRequest request,
        HttpServletResponse response) {
        User user = userService.signIn(request);
        response.addCookie(tokenManager.makeCookie(user));
        return ResponseHandler.generateResponse(ResponseCode.SIGN_IN_SUCCESS, HttpStatus.OK, user);
    }

    @Auth
    @GetMapping()
    public ResponseEntity<CommonResponse> getUserInfo(@UserResolver User user) {
        return ResponseHandler.generateResponse(ResponseCode.USER_INFO_SUCCESS, HttpStatus.OK,
            user);
    }

    @DeleteMapping("/logout")
    public ResponseEntity<CommonResponse> logout(HttpServletResponse response) {
        response.addCookie(tokenManager.resetCookie());
        return ResponseHandler.generateResponse(ResponseCode.LOGOUT_SUCCESS, HttpStatus.OK, null);
    }

    @Auth
    @PutMapping()
    public ResponseEntity<CommonResponse> update(@UserResolver User user, UserUpdateRequest request,
        MultipartFile profile) {
        userService.updateUser(user, request, profile);
        return ResponseHandler.generateResponse(ResponseCode.USER_UPDATE_SUCCESS, HttpStatus.OK,
            null);
    }

    @GetMapping("/nickname/random")
    public ResponseEntity<CommonResponse> getNickname() {
        return ResponseHandler.generateResponse(ResponseCode.NICkNAME_GENERATE_SUCCESS,
            HttpStatus.OK,
            userService.makeNickname());
    }

    @PostMapping("/password")
    public ResponseEntity<CommonResponse> findPassword(@UserResolver User user,
        @RequestBody SignUpRequest request)
        throws MessagingException {
        String password = userService.findPassword(request.getEmail());
        emailService.sendPasswordMail(request.getEmail(),
            userService.findUserByEmail(request.getEmail()), password);
        return ResponseHandler.generateResponse(ResponseCode.MAIL_SEND_SUCCESS, HttpStatus.OK,
            null);
    }

    @Auth
    @PostMapping("/password/verify")
    public ResponseEntity<CommonResponse> verifyPassword(@UserResolver User user,
        @RequestBody SignUpRequest request) {
        if (userService.isSamePassword(request.getPassword(), user)) {
            return ResponseHandler.generateResponse(ResponseCode.PASSWORD_CHECK_SUCCESS,
                HttpStatus.OK,
                true);
        }
        return ResponseHandler.generateResponse(ResponseCode.INCORRECT_PASSWORD, HttpStatus.OK,
            false);
    }

    @Auth
    @PostMapping("/email/verify")
    public ResponseEntity<CommonResponse> verifyEmail(@UserResolver User user,
        @RequestBody SignUpRequest request)
        throws MessagingException {

        emailService.sendSpareMail(request.getEmail(), user);
        return ResponseHandler.generateResponse(ResponseCode.MAIL_SEND_SUCCESS, HttpStatus.OK,
            null);
    }

}
