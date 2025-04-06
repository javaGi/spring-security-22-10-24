package com.zoho.repository;

import com.zoho.entity.SignUpUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SignUpUserRepository extends JpaRepository<SignUpUser, Long> {
  Optional<SignUpUser> findByUsername(String username);
    Optional<SignUpUser> findByEmail(String email);

}