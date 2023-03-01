package com.mehrab.springdemo.config;

import com.mehrab.springdemo.security.person.MehrabAuthenticationProvider;
import com.mehrab.springdemo.security.robot.RobotLoginConfigurer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationEventPublisher publisher) throws Exception {
        {
            http.getSharedObject(AuthenticationManagerBuilder.class)
                    .authenticationEventPublisher(publisher);
        }
        return http
                .authorizeHttpRequests((authz) -> {
                        authz.requestMatchers("/").permitAll();
                        authz.requestMatchers("/error").permitAll();
                        authz.requestMatchers("/favicon.ico").permitAll();
                        authz.anyRequest().authenticated();
                }).formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .apply(new RobotLoginConfigurer().password("beep-boop").password("boop-beep")).and() // (3)
                .authenticationProvider(new MehrabAuthenticationProvider())
//                .oauth2Login(Customizer.withDefaults()) // we tell filterchain that login with openID (built on top of oauth2)
                .build();

    }


    // provider manager when user logged in produces an event and when faild too produces event (spring event) for listen those event...
    @Bean
    public ApplicationListener<AuthenticationSuccessEvent> successListenner() {
        return (event) -> {
            System.out.println(String.format("👌 SUCCESS [%s] %s", event.getAuthentication().getClass().getSimpleName(), event.getAuthentication().getName()));
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}
