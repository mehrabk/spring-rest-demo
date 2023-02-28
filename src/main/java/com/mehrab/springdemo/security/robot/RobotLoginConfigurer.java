package com.mehrab.springdemo.security.robot;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.ArrayList;
import java.util.List;

public class RobotLoginConfigurer extends AbstractHttpConfigurer<RobotLoginConfigurer, HttpSecurity> {
    private final List<String> passwords = new ArrayList<>();
    @Override
    public void init(HttpSecurity http) throws Exception {
        // step1. initializes a bunch of objects
        super.init(http.authenticationProvider(new RobotAuthenticationProvider(passwords)));
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        // step2. this also initializes objects . but can reuse object from step 1. even from other configurers
        // spring security re-implemention of application context spring (kind of beans)
        // like repository of object that you put things in them.
        AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
        super.configure(http.addFilterBefore(new RobotFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class));
    }

    public RobotLoginConfigurer password(String password) {
        this.passwords.add(password);
        return this;
    }
}
