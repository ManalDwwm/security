package com.JynInfoConseil.users.sevice;


import com.JynInfoConseil.users.entities.Role;
import com.JynInfoConseil.users.entities.User;

public interface UserService {
    User saveUser(User user);
    User findUserByUsername(String username);
    Role addRole(Role role);
    User addRoleToUser(String username, String rolename);
}