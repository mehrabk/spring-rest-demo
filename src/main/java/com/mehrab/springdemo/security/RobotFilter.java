package com.mehrab.springdemo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;

public class RobotFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "x-robot-password";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        System.out.println("🤖 Hello from the robot filter");
        // 0. Should execute filter (request come from browser dont have robot header and we want continue to next filters)?
        if(!Collections.list(request.getHeaderNames()).contains(HEADER_NAME)) {
            filterChain.doFilter(request, response);
            return;
        }

        var password = request.getHeader(HEADER_NAME);
        if (!"beep-boop".equals(password)){
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/plain");
            response.getWriter().println("you are not Ms Robot 🤖⛔");
            return;
        }
        var newContext = SecurityContextHolder.createEmptyContext();
        newContext.setAuthentication(new RobotAuthentication());
        SecurityContextHolder.setContext(newContext);
        filterChain.doFilter(request, response);
    }
}
