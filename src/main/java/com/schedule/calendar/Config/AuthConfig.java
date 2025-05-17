package com.schedule.calendar.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
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
    private final MyUserDetailsService userDetailsService;
    
    public AuthConfig(MyUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests -> {
                    authorizeRequests.requestMatchers("/", "/auth/**", "/css/**", "/js/**", "/fragments/**", "login/**", "logout/**", "auth/signup").permitAll();
                    authorizeRequests.requestMatchers("/admin/**").hasRole("ADMIN");
//                    authorizeRequests.requestMatchers("/calendar/**").authenticated());
                    authorizeRequests.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .formLogin(httpSecurityFormLoginConfigurer ->
                {
                    httpSecurityFormLoginConfigurer
                            .loginPage("/auth/login")
                            .loginProcessingUrl("/login")
                            .failureUrl("/auth/login?error")
                            .successHandler(new MyAuthSuccessHandler())
                            .permitAll();
                })
                .build();
    }
    
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }
    
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
}