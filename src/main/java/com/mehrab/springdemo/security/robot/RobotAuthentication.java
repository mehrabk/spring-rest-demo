package com.mehrab.springdemo.security.robot;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class RobotAuthentication implements Authentication {

    private final boolean isAuthenticated;
    private final List<GrantedAuthority> authorities;
    private final String password;

    public RobotAuthentication(List<GrantedAuthority> authorities, String password) {
        this.authorities = authorities;
        this.password = password;
        // if password is null then authenticated
        this.isAuthenticated = password == null;
    }

    // for test -> two state of authenticated an unautenticated RobotAuthentication
    public static RobotAuthentication unAuthenticated(String password) {
        return new RobotAuthentication(Collections.emptyList(), password);
    }

    public static RobotAuthentication authenticated() {
        return new RobotAuthentication(AuthorityUtils.createAuthorityList("ROLE_robot"), null);
    }

    @Override
    public String getName() {
        return "Ms Robot 🤖";
    }

    @Override
    public Object getPrincipal() {
        return this.getName();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        throw new IllegalArgumentException("Dont");
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    public String  getPassword() {
        return password;
    }
}
