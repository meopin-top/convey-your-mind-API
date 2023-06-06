package muffintop.cym.api.component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Base64;
import java.util.Date;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.domain.Token;
import muffintop.cym.api.domain.User;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class JwtTokenManager {

    private String secretKey = "secret";

    // 토큰 유효시간 60분, 1일
    private long accessTokenTime = 60 * 60 * 1000L;
    private long refreshTokenTime = 24 * 60 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }

    // JWT Access 토큰 생성
    public String createAccessToken(User user) {
        Claims claims = Jwts.claims().setSubject("User");
        claims.put("userId", user.getUserId())
        claims.put("authMethod",user.getAuthMethod());
        Date now = new Date();
        return Jwts.builder()
            .setClaims(claims)
            .setIssuedAt(now) // 토큰 발행 시간 정보
            .setExpiration(new Date(now.getTime() + accessTokenTime)) // set Expire Time
            .signWith(SignatureAlgorithm.HS256, secretKey)  // 사용할 암호화 알고리즘과
            .compact();
    }

    // JWT Refresh 토큰 생성
    public String createRefreshToken() {
        Date now = new Date();
        return Jwts.builder()
            .setIssuedAt(now)
            .setExpiration(new Date(now.getTime() + refreshTokenTime))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }

    public Token generateNewToken(User user){
        return Token.builder()
            .accessToken(createAccessToken(user))
            .refreshToken(createRefreshToken())
            .build();
    }
}
