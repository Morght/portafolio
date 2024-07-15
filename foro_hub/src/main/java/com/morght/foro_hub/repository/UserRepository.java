package com.morght.foro_hub.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import com.morght.foro_hub.models.User;

public interface UserRepository extends JpaRepository<User, Long> {

    UserDetails findByEmail(String email);

}
