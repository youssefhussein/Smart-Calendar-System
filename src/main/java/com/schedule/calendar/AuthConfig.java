package com.schedule.calendar;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class AuthConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(authorizeRequests ->{
                    authorizeRequests.requestMatchers("/" , "/auth/**" , "/css/**" , "/js/**").permitAll();
                    authorizeRequests.anyRequest().authenticated();
                        })
                .oauth2Login(httpSecurityOAuth2LoginConfigurer -> {
                    httpSecurityOAuth2LoginConfigurer.loginPage("/auth/login");
                    httpSecurityOAuth2LoginConfigurer.successHandler((request, response, authentication) -> {
                        response.sendRedirect("/calendar");
                    });
                })
                .build();
    }
}