package muffintop.cym.api.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.JwtTokenManager;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.exception.UnAuthorizedException;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenManager jwtTokenManager;

    public static final String USER_KEY = "user";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) {

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        Auth auth = handlerMethod.getMethodAnnotation(Auth.class);

        Cookie[] cookies = request.getCookies();

        User user = jwtTokenManager.validateToken(cookies);

        request.setAttribute(USER_KEY, user);

        //로그인이 필요없는 컨트롤러면 통과
        if (auth == null) {
            return true;
        } else {
            if (user == null) {
                throw new UnAuthorizedException();
            }
        }

        return true;
    }


}