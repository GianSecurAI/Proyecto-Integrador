package com.example.Reyna.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.Reyna.model.User;

public interface UserRepository extends JpaRepository<User, Long> { // CAMBIO CRUCIAL: Extender JpaRepository con Long
    Optional<User> findByCorreo(String correo);
    // JpaRepository ya proporciona findById, existsById, deleteById, etc.
    // No necesitas declararlos explícitamente aquí.
}