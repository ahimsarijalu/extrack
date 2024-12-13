package com.ahimsarijalu.extrack.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
//    List<User> findAllByNameOrderBy(String name);
    UserDetails findByEmail(String email);
}
