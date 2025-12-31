package org.xproce.firesafe_audit.service.auth;

import org.xproce.firesafe_audit.dao.entities.User;
import org.xproce.firesafe_audit.dto.auth.LoginRequest;
import org.xproce.firesafe_audit.dto.auth.LoginResponse;
import org.xproce.firesafe_audit.dto.auth.RegisterRequest;

public interface IAuthService {
    LoginResponse login(LoginRequest request);
    User register(RegisterRequest request);
    User getCurrentUser();
}