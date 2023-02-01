package com.mehrab.springdemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

// authentication.getPrincipal() or authentication.getDetails() returned Object so it can be extendable of rich of properties
// securityContextHolder is a thread local variable that means its only available in the thread thats processing the request.
// if i spawn more thread that going to talk to database or do more calls they do not have access to the security context.
// securityContext cleaned after the request is processed then can be used for other next requests(doesnt steal authentication)

@RestController
public class SecurityDemoController {

    @RequestMapping(method = RequestMethod.GET, path = "/public")
    public String publicPage() {
        return "hello mehrab";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/private")
    public String privatePage(Authentication authentication) throws InterruptedException {
        // way2(injecting) -> same as authentication
        Authentication authentication1 = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("OUTSIDE THREAD -> " + authentication1);

        Thread thread = new Thread(() -> {
            Authentication authentication2 = SecurityContextHolder.getContext().getAuthentication();
            System.out.println("IN THREAD -> " + authentication2);
        });
        thread.start();
        thread.join();

        System.out.println("Authentication Details -> " + authentication.getDetails());

        return "this page shoulde be private😍😍 \n welcome to the VIP room ~[" + getName(authentication) + "]~";
    }
    
    public static String getName(Authentication authentication) {
        // if login with OID get the user email or get name
        return Optional.of(authentication.getPrincipal())
                .filter(OidcUser.class::isInstance)
                .map(OidcUser.class::cast)
                .map(OidcUser::getEmail)
                .orElseGet(authentication::getName);

    }
}
