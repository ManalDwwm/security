package com.JynInfoConseil.users.repos;


import com.JynInfoConseil.users.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByRole(String role);
}