package edu.bbte.idde.keim2152.spring.auth.payload.jwt;

import edu.bbte.idde.keim2152.spring.service.UserDetailsImp;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Component
@Slf4j
public class JwtManager {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpiration}")
    private Integer jwtExpiration;

    public String getUsernameFromToken(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload().getExpiration();
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetailsImp userPrincipal = (UserDetailsImp) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .expiration(Date.from(Instant.now().plusMillis(jwtExpiration)))
                .issuedAt(new Date())
                .claim("role", userPrincipal.getAuthorities().stream().findFirst().get())
                .signWith(key())
                .compact();
    }

    private SecretKey key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public Boolean validateToken(String token) {
        return !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        Date now = new Date();
        log.debug("Checking expiration");
        return  now.after(getExpirationDateFromToken(token));
    }

    public String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        if (cookie == null) {
            return null;
        }
        log.debug("Cookie Found");
        return cookie.getValue();
    }

    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parser().verifyWith(key()).build().parseSignedClaims(token).getPayload();
        if (claims == null) {
            log.error("No claims set");
            return null;
        }        
        return (String) claims.get("role");
    }

}
