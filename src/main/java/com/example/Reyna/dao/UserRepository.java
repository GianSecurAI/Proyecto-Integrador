package com.example.Reyna.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Reyna.model.User;

public interface UserRepository extends JpaRepository<User,Integer> {
    Optional<User> findByCorreo(String nombre_completo);
}
