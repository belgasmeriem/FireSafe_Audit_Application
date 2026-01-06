package org.xproce.firesafe_audit.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.service.user.IUserService;

@Service("userSecurityService")
@RequiredArgsConstructor
public class UserSecurityService {

    private final IUserService userService;


    public boolean isOwner(Long userId) {
        try {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()) {
                return false;
            }

            String username = auth.getName();
            UserDTO currentUser = userService.getUserByUsername(username);

            return currentUser != null && currentUser.getId().equals(userId);
        } catch (Exception e) {
            return false;
        }
    }
}