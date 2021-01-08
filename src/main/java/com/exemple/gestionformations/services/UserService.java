package com.exemple.gestionformations.services;

import com.exemple.gestionformations.dto.UserRegistrationDto;
import com.exemple.gestionformations.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;



public interface UserService extends UserDetailsService{
    User save(UserRegistrationDto registrationDto);
}
