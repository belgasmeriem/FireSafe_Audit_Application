package org.xproce.firesafe_audit.web.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;


@Slf4j
@Component
public class RoleBasedAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        String targetUrl = determineTargetUrl(authentication);

        log.info("User {} authenticated successfully. Redirecting to: {}",
                authentication.getName(), targetUrl);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    private String determineTargetUrl(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        log.debug("User roles: {}", authorities);

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();

            if (role.equals("ROLE_ADMIN")) {
                log.info("Redirecting ADMIN to /dashboard");
                return "/dashboard";
            } else if (role.equals("ROLE_MANAGER")) {
                log.info("Redirecting MANAGER to /dashboard");
                return "/dashboard";
            } else if (role.equals("ROLE_AUDITOR")) {
                log.info("Redirecting AUDITOR to /audits/mes-audits");
                return "/audits/mes-audits";
            }
        }

        log.warn("No specific role found, redirecting to /dashboard");
        return "/dashboard";
    }
}