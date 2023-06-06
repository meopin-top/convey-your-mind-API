package muffintop.cym.api.controller;

import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @GetMapping("/s")
    public String test2(HttpServletRequest request){
        System.out.println(request.getCookies().stream());
        return "good";
    }


}
