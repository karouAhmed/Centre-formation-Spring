package com.exemple.gestionformations.repository;

import com.exemple.gestionformations.entities.Role;
import com.exemple.gestionformations.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
}
