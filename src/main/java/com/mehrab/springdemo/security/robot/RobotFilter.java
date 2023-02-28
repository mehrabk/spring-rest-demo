package com.mehrab.springdemo.security.robot;

import com.mehrab.springdemo.security.robot.RobotAuthentication;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class RobotFilter extends OncePerRequestFilter {

    private final String HEADER_NAME = "x-robot-password";

    // authentication manager => transform an authentication request into an authentication object

    // if we want robot filter to use authentication manager
    private final AuthenticationManager authenticationManager;

    public RobotFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

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
        RobotAuthentication authRequest = RobotAuthentication.unAuthenticated(password);

        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);
//            Authentication authentication = authenticationManager.authenticate(new RobotAuthentication(Collections.emptyList(), password));
            var newContext = SecurityContextHolder.createEmptyContext();
            newContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(newContext);
            filterChain.doFilter(request, response);
            return;
        } catch (AuthenticationException e) {
            response.setStatus(HttpStatus.FORBIDDEN.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/plain");
            response.getWriter().println(e.getMessage());
            return;
        }
    }
}
