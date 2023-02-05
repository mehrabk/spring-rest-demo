package com.mehrab.springdemo.config;

import com.mehrab.springdemo.security.RobotFilter;
import com.mehrab.springdemo.security.authenticationProvider.MehrabAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// SecurityContextHolder.class ->  public static SecurityContext getContext(){...}
// SecurityContext -> is Holder that has the Authentication inside. this is only true in the servlet world and alive in the thread(anything in thread that process on request can access to this security context (inside controller))
// Thread-local, not propagated to child  threads - cleared after requests is 100% processed

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/").permitAll();
                        authz.requestMatchers("/error").permitAll();
                        authz.requestMatchers("/favicon.ico").permitAll();
                        authz.anyRequest().authenticated();
                }).formLogin(Customizer.withDefaults()) // set springboot default login page for forbidden pages
                .addFilterBefore(new RobotFilter(), UsernamePasswordAuthenticationFilter.class)
                .authenticationProvider(new MehrabAuthenticationProvider())
//                .oauth2Login(Customizer.withDefaults()) // we tell filterchain that login with openID (built on top of oauth2)
                .build();

    }


    // UserDetailsService -> repository of users
    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(User.builder()
                .username("mehrab2")
                .password("{noop}zaq123")
                .authorities("ROLE_user")
                .build());
    }
}
