package edu.bbte.idde.keim2152.spring.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
public class LoggerInterceptor implements HandlerInterceptor {

    @Override
    public void afterCompletion(
            HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        if (ex != null) {
            log.error(ex.getMessage());
        }
        log.info(request.getMethod() + " " + request.getRequestURL() + " " + response.getStatus());
    }
}
