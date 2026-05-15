package com.skillexchange.interceptor;

import com.skillexchange.utils.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Resource
    private JwtUtil jwtUtil;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        // 公开接口，无需登录
        if (isPublicEndpoint(requestURI)) {
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                if (jwtUtil.validateToken(token)) {
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    String username = jwtUtil.getUsernameFromToken(token);
                    request.setAttribute("userId", userId);
                    request.setAttribute("username", username);
                }
            }
            return true;
        }

        // 需要登录的接口
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录或token无效\"}");
            return false;
        }

        String token = authHeader.substring(7);
        if (!jwtUtil.validateToken(token)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"token已过期或无效\"}");
            return false;
        }

        Long userId = jwtUtil.getUserIdFromToken(token);
        String username = jwtUtil.getUsernameFromToken(token);
        request.setAttribute("userId", userId);
        request.setAttribute("username", username);

        return true;
    }

    private boolean isPublicEndpoint(String uri) {
        return uri.equals("/api/user/login") ||
                uri.equals("/api/user/register") ||
                uri.equals("/api/skills") ||
                uri.startsWith("/api/skills/") ||
                uri.equals("/api/skill-types") ||
                uri.equals("/api/skill-types/active") ||
                uri.startsWith("/api/skill-types/") ||
                uri.startsWith("/api/comments/skill/") ||
                uri.startsWith("/api/upload/") ||
                uri.startsWith("/upload/");
    }
}