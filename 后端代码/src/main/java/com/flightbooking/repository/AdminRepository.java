package com.flightbooking.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.flightbooking.entity.Admin;

public interface AdminRepository extends JpaRepository<Admin, Integer> {
    
    Optional<Admin> findByUsername(String username);
    
    Optional<Admin> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
}