package muffintop.cym.api.controller;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import muffintop.cym.api.component.JwtTokenManager;
import muffintop.cym.api.domain.MagicLink;
import muffintop.cym.api.domain.Token;
import muffintop.cym.api.repository.MagicLinkRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/magic-link")
@RestController
public class MagicLinkController {

    private final MagicLinkRepository magicLinkRepository;
    private final JwtTokenManager tokenManager;

    @Value("${host.name}")
    private String host;

    @GetMapping("/{magicLink}")
    private ResponseEntity<Object> magicLink(@PathVariable String magicLink,
        HttpServletResponse response)
        throws URISyntaxException {

        MagicLink targetMagicLink = magicLinkRepository.findById(magicLink).orElse(null);
        URI redirectUri = new URI(host);
        if (targetMagicLink.getExpiredDatetime().isBefore(LocalDateTime.now())) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        }
        Token token = tokenManager.generateNewToken(targetMagicLink.getUser());
        Cookie accessTokenCookie = new Cookie("AccessToken", token.getAccessToken());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(redirectUri);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
    }

}
