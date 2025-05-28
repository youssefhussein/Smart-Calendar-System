package com.schedule.calendar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class AuthConfig {
    private final UserDetailsService myUserDetailsService; // Ensure this is injected with your MyUserDetailsService
    private final MyAuthSuccessHandler myAuthSuccessHandler;

    public AuthConfig(UserDetailsService myUserDetailsService, MyAuthSuccessHandler myAuthSuccessHandler) {
        this.myUserDetailsService = myUserDetailsService;
        this.myAuthSuccessHandler = myAuthSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests
                            .requestMatchers(
                                    "/",
                                    "/auth/login",      // Permit access to the login page
                                    "/auth/signup",     // Permit access to the signup page
                                    "/login",           // Permit access to the login processing URL
                                    // Note: /auth/register (if that's your signup POST URL) should also be permitted
                                    // or handled by the fact that /auth/** is already permitted here.
                                    "/css/**",
                                    "/js/**",
                                    "/img/**",
                                    "/webjars/**"
                            ).permitAll()
                            .requestMatchers("/admin/**").hasRole("ADMIN")
                            .requestMatchers("/calendar/**", "/profile").authenticated()
                            .anyRequest().authenticated();
                })
                .formLogin(formLogin -> formLogin
                        .loginPage("/auth/login")
                        .loginProcessingUrl("/login")
                        .failureUrl("/auth/login?error")
                        .successHandler(myAuthSuccessHandler)
                        // .permitAll() // Already handled by authorizeHttpRequests for clarity
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/auth/login?logout")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(myUserDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}