package muffintop.cym.api.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.ResponseHandler;
import muffintop.cym.api.controller.request.SignUpRequest;
import muffintop.cym.api.controller.response.CommonResponse;
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

    @PostMapping("/sign-up")
    public ResponseEntity<CommonResponse> signUp(@RequestBody SignUpRequest request){
        return ResponseHandler.generateResponse(userService.signUp(request), HttpStatus.OK,null);
    }


}
