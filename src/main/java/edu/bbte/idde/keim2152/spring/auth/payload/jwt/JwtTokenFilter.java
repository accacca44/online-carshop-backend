package edu.bbte.idde.keim2152.spring.auth.payload.jwt;

import edu.bbte.idde.keim2152.spring.service.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;

@Slf4j
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtManager jwtManager;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        try {
            String jwt = getJwtFromCookie(request);
            log.debug("Checking jwt validation");
            log.debug("JWT " + jwt);
            if (jwt != null && jwtManager.validateToken(jwt)) {
                log.info("Valid Token, should have access!");
                String username = jwtManager.getUsernameFromToken(jwt);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (UsernameNotFoundException e) {
            log.error("User not found");
        }
        filterChain.doFilter(request, response);
    }


    /* Method for getting the JWT Token from the Authorization header */
    //     private String getJwt(HttpServletRequest request) {
    //         String headerAuth = request.getHeader("Authorization");
    //
    //         if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
    //             log.info(headerAuth.substring(7));
    //             return headerAuth.substring(7);
    //         }
    //
    //         return null;
    //     }

    private String getJwtFromCookie(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, "token");
        if (cookie == null) {
            return null;
        }
        log.debug("Cookie Found");
        return cookie.getValue();
    }
}
