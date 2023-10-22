package muffintop.cym.api.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.controller.ResponseExceptionController;
import muffintop.cym.api.domain.Token;
import muffintop.cym.api.domain.User;
import muffintop.cym.api.domain.key.UserPk;
import muffintop.cym.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenManager {

    @Value("${jwt.secret}")
    private String secretKey;

    // 토큰 유효시간 90일
    private long accessTokenTime = 90 * 24 * 60 * 60 * 1000L;

    private final static String DOMAIN  = "localhost";

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenManager.class);

    private final UserService userService;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Access 토큰 생성
    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject("User");
        claims.put("userId", user.getId());
        claims.put("authMethod", user.getAuthMethod());
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + accessTokenTime)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
            .compact();
    }

//    // JWT Refresh 토큰 생성
//    public String createRefreshToken() {
//        Date now = new Date();
//        return Jwts.builder()
//            .setIssuedAt(now)
//            .setExpiration(new Date(now.getTime() + refreshTokenTime))
//            .signWith(SignatureAlgorithm.HS256, secretKey)
//            .compact();
//    }

    public Token generateNewToken(User user) {
        return Token.builder()
            .accessToken(createAccessToken(user))
            .build();
    }


    public User validateToken(Cookie[] cookies){
        if(cookies == null){
            LOGGER.info("Cookie is null");
            return null;
        }

        Cookie accessTokenCookie = Arrays.stream(cookies).filter(cookie -> "AccessToken".equals(cookie.getName())).findFirst().orElse(null);

        if(accessTokenCookie == null){
            LOGGER.info("Access Cookie is null");
            return null;
        }

        String accessToken = accessTokenCookie.getValue();

        try{
            Claims claims = parseClaims(accessToken);
            String id = (String) claims.get("userId");
            String authMethod = (String) claims.get("authMethod");
            UserPk userPk = new UserPk(id,authMethod.charAt(0));

            User user = userService.getUserByUserPk(userPk);

            return user;
        } catch (ExpiredJwtException e) {
            LOGGER.info("Expired JWT Token", e);
            return null;
        } catch (UnsupportedJwtException e) {
            LOGGER.info("Unsupported JWT Token", e);
            return null;
        } catch (IllegalArgumentException e) {
            LOGGER.info("JWT claims string is empty.", e);
            return null;
        } catch (Exception e) {
            LOGGER.info("Unknown Exception", e);
            return null;
        }
    }

    private Claims parseClaims(String accessToken){
        Claims claims = Jwts.parser()
            .setSigningKey(secretKey)
            .parseClaimsJws(accessToken)
            .getBody();
        return claims;
    }

    public Cookie makeCookie(User user) {
        Token token = generateNewToken(user);
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        accessTokenCookie.setDomain("34.64.92.123");
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        return accessTokenCookie;
    }

    public Cookie resetCookie() {
        Cookie accessTokenCookie = new Cookie("AccessToken", null);
        accessTokenCookie.setDomain("34.64.92.123");
        accessTokenCookie.setHttpOnly(false);
        accessTokenCookie.setSecure(false);
        accessTokenCookie.setPath("/");
        return accessTokenCookie;
    }


}
