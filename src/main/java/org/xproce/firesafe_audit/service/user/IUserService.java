package org.xproce.firesafe_audit.service.user;

import org.xproce.firesafe_audit.dao.enums.RoleType;
import org.xproce.firesafe_audit.dto.user.UserCreateDTO;
import org.xproce.firesafe_audit.dto.user.UserDTO;
import org.xproce.firesafe_audit.dto.user.UserUpdateDTO;

import java.util.List;

public interface IUserService {
    List<UserDTO> getAllUsers();
    List<UserDTO> getActiveUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByUsername(String username);
    List<UserDTO> getUsersByRole(RoleType roleType);
    List<UserDTO> searchUsers(String search);
    UserDTO createUser(UserCreateDTO dto);
    UserDTO updateUser(Long id, UserUpdateDTO dto);
    void deleteUser(Long id);
    void changePassword(Long id, String newPassword);
    long countByRole(RoleType roleType);
}