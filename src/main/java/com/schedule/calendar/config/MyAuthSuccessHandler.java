package com.schedule.calendar.config;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class MyAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
   
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
        if (isAdmin) {
            getRedirectStrategy().sendRedirect(request, response, "/admin/users");
        } else {
            getRedirectStrategy().sendRedirect(request, response, "/calendar");
        }
        
    }
}