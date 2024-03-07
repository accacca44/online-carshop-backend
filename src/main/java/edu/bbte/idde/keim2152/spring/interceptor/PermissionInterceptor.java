package edu.bbte.idde.keim2152.spring.interceptor;

import edu.bbte.idde.keim2152.spring.service.UserDetailsImp;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class PermissionInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        SecurityContext context = SecurityContextHolder.getContext();
        Authentication auth = context.getAuthentication();
        UserDetailsImp user = (UserDetailsImp) auth.getPrincipal();
        String role = String.valueOf(user.getAuthorities().stream().findFirst().get());

        // Reading data is permitted, but only the user can only manipulate their own car listings
        log.info(request.getMethod());
        if (!"GET".equals(request.getMethod())) {
            if ("user".equals(role)) {
                return handleUser(request, response, user);
            }
            return handleAdmin(request, response);
        }
        return true;
    }

    private boolean handleUser(HttpServletRequest request, HttpServletResponse response, UserDetailsImp user) {
        String uri = request.getRequestURI();
        log.debug(uri);
        String pathParamId = uri.split("/")[3];
        if ("".equals(pathParamId) || pathParamId == null) {
            log.error("No userId path param was given!");
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }

        boolean sameUser;
        try {
            sameUser = user.getId() == Long.parseLong(pathParamId);
        } catch (NumberFormatException e) {
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            return false;
        }

        if (sameUser) {
            response.setStatus(HttpStatus.OK.value());
            return true;
        } else {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }
    }

    private boolean handleAdmin(HttpServletRequest request, HttpServletResponse response) {
        if (!"DELETE".equals(request.getMethod())) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        String uri = request.getRequestURI();
        if (uri.contains("carlistings")) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            return false;
        }

        response.setStatus(HttpStatus.OK.value());
        return true;
    }
}
