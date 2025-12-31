package com.cloud.backend.global.filter;

import com.cloud.backend.global.security.LoginMember;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Slf4j
public class MdcFilter extends OncePerRequestFilter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {

            String requestId = Optional.ofNullable(request.getHeader(REQUEST_ID_HEADER)).orElse(UUID.randomUUID().toString());
            MDC.put("requestId", requestId);


            String userId = resolveUserId();
            MDC.put("userId", userId);

            filterChain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }


    /**
     * SecurityContext에서 사용자 ID 추출
     */
    private String resolveUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            Object principal = auth.getPrincipal();

            if (principal instanceof LoginMember loginMember) {
                return String.valueOf(loginMember.getEmail());
            }
        }

        return "anonymous";
    }
}
