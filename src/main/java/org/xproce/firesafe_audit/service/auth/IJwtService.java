package org.xproce.firesafe_audit.service.auth;

import org.springframework.security.core.userdetails.UserDetails;
import java.util.Date;

public interface IJwtService {
    String generateToken(UserDetails userDetails);
    String generateRefreshToken(UserDetails userDetails);
    String extractUsername(String token);
    Date extractExpiration(String token);
    Boolean validateToken(String token, UserDetails userDetails);
}