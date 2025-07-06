package com.schedule.calendar.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyAuthSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    public MyAuthSuccessHandler() {
        // The parent class SavedRequestAwareAuthenticationSuccessHandler defaults
        // alwaysUseDefaultTarget to false, which is typically what we want.
        // It will try to redirect to the originally requested URL if one was saved.
        // We will set a default target URL that will be used if no saved request exists
        // or if we explicitly decide to always use our role-based target.
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        String roleBasedTargetUrl = determineTargetUrl(authentication);

        // Option 1: Always redirect to the role-based target URL, ignoring any saved request.
        // This is simpler if your requirement is strict role-based redirection.
        clearAuthenticationAttributes(request); // Good practice
        getRedirectStrategy().sendRedirect(request, response, roleBasedTargetUrl);

        // Option 2: Use role-based URL as a fallback if no saved request,
        // otherwise redirect to the originally intended protected page.
        // To use this, comment out Option 1 and uncomment the lines below:
        // this.setDefaultTargetUrl(roleBasedTargetUrl);
        // super.onAuthenticationSuccess(request, response, authentication);
    }

    protected String determineTargetUrl(Authentication authentication) {
        boolean isAdmin = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(authorityName -> authorityName.equals("ROLE_ADMIN"));

        if (isAdmin) {
            return "/admin/users";
        } else {
            return "/calendar";
        }
    }
}