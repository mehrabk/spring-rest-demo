package com.mehrab.springdemo.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;

public class RobotAuthentication implements Authentication {
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
        return true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.createAuthorityList("ROLE_robot");
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
}
