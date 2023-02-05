package com.mehrab.springdemo.security.authenticationProvider;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.AuthorityUtils;

public class MehrabAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("😎 Hello from the MehrabAuthenticationProvider");
        var username = authentication.getName();
        if("mehrab".equals(username)) {
            return UsernamePasswordAuthenticationToken.authenticated("mehrab",null, AuthorityUtils.createAuthorityList("ROLE_admin"));
        }
        // delegate to rest
        return null;
    }

    // which type of authentication objects are we going to look at with this authentication (in this example only UsernamePasswordAuthenticationToken)
    @Override
    public boolean supports(Class<?> authentication) {
        // if login with robot its not mehrab
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
