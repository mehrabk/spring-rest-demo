package com.mehrab.springdemo.config;

import com.mehrab.springdemo.security.RateLimitAuthenticationProvider;
import com.mehrab.springdemo.security.robot.RobotFilter;
import com.mehrab.springdemo.security.person.MehrabAuthenticationProvider;
import com.mehrab.springdemo.security.robot.RobotAuthenticationProvider;
import com.mehrab.springdemo.security.robot.RobotLoginConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

// SecurityContextHolder.class ->  public static SecurityContext getContext(){...}
// SecurityContext -> is Holder that has the Authentication inside. this is only true in the servlet world and alive in the thread(anything in thread that process on request can access to this security context (inside controller))
// Thread-local, not propagated to child  threads - cleared after requests is 100% processed

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationEventPublisher publisher) throws Exception {
        {
            http.getSharedObject(AuthenticationManagerBuilder.class)
                    .authenticationEventPublisher(publisher);
        }

        // (1)
//        ProviderManager providerManager = new ProviderManager(new RobotAuthenticationProvider(List.of("beep-boop", "boop-beep")));
        // we are going to wire the event publisher in it
//        providerManager.setAuthenticationEventPublisher(publisher);

        // (2)
//        var configurer = new RobotLoginConfigurer().password("beep-boop").password("boop-beep");


        return http
                .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/").permitAll();
                        authz.requestMatchers("/error").permitAll();
                        authz.requestMatchers("/favicon.ico").permitAll();
                        authz.anyRequest().authenticated();
                }).formLogin(Customizer.withDefaults()) // set springboot default login page for forbidden pages
                .httpBasic(Customizer.withDefaults()) // can login with curl (authentication provider doesnt care aboute the request)
//                .addFilterBefore(new RobotFilter(providerManager), UsernamePasswordAuthenticationFilter.class) //(1)
//                .apply(configurer).and() // create a custom login configure with correct pattern (2)
                .apply(new RobotLoginConfigurer().password("beep-boop").password("boop-beep")).and() // (3)
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

    // provider manager when user logged in produces an event and when faild too produces event (spring event) for listen those event...
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successListenner() {
        return (event) -> {
            System.out.println(String.format("👌 SUCCESS [%s] %s", event.getAuthentication().getClass().getSimpleName(), event.getAuthentication().getName()));
        };
    }
}
