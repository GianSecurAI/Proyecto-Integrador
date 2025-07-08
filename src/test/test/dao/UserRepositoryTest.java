package com.example.reyna.dao;

import com.example.reyna.user.Role;
import com.example.reyna.user.User;
import com.example.reyna.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testFindByNombreCompleto() {
        User user = User.builder()
                .nombreCompleto("Juan Perez")
                .correo("juan@mail.com")
                .password("1234")
                .role(Role.USER)
                .build();

        userRepository.save(user);

        Optional<User> found = userRepository.findByNombreCompleto("Juan Perez");

        assertTrue(found.isPresent());
        assertEquals("juan@mail.com", found.get().getCorreo());
    }
}
