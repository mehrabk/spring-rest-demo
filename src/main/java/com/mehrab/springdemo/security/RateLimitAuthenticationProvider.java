package com.mehrab.springdemo.security;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RateLimitAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationProvider delegate;
    private final Map<String, Instant> cache = new ConcurrentHashMap<>();

    public RateLimitAuthenticationProvider(AuthenticationProvider delegate) {
        this.delegate = delegate;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var parentAuth = delegate.authenticate(authentication);

        if(parentAuth == null) return null;

        if (update(parentAuth)) {
            return parentAuth;
        }
        throw new BadCredentialsException("🤦‍ Not So Fast");
    }

    private boolean update(Authentication parentAuth) {
        var now = Instant.now();
        var previuosAuthTime = cache.get(parentAuth.getName());
        cache.put(parentAuth.getName(), now);
        return previuosAuthTime == null || previuosAuthTime.plus(Duration.ofMinutes(1)).isBefore(now);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return delegate.supports(authentication);
    }
}
