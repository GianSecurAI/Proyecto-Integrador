package com.example.Reyna.Service;

import com.example.Reyna.dao.RolRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RolRepository rolRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        // Nota: El ID en el modelo User es 'long id_usuario', pero UserRepository usa 'Integer'.
    // Se realiza una conversión a intValue(). Asegúrate de que los IDs no excedan el rango de Integer.
        return userRepository.findById(id).orElse(null);
    }
    public Optional<User> findByCorreo(String correo) {
        return userRepository.findByCorreo(correo);
    }
       public User updateUser(Long id, User userDetails) {
        User existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        // Un administrador solo debe actualizar estos campos específicos para un cliente.
        // Esto evita cambiar accidentalmente la contraseña, el rol o el estado.
        existingUser.setNombre(userDetails.getNombre());
        existingUser.setApellido(userDetails.getApellido());
        existingUser.setCorreo(userDetails.getCorreo());
        existingUser.setTelefono(userDetails.getTelefono());
        existingUser.setDireccion(userDetails.getDireccion());

        return userRepository.save(existingUser);
    }

    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
        throw new RuntimeException("Usuario no encontrado con ID: " + id);
        }
        userRepository.deleteById(id);
    }

    // Método para buscar clientes por nombre, apellido, email o teléfono
    public List<User> searchClients(String query) {
        Rol clientRole = rolRepository.findById(3L) // Asumiendo que el ID del rol CLIENTE es 3
                .orElseThrow(() -> new RuntimeException("Rol con ID 3 (CLIENTE) no encontrado"));
       if (query == null || query.trim().isEmpty()) {
            return userRepository.findAll().stream()
                    .filter(user -> user.getRol() != null && user.getRol().equals(clientRole))
                    .collect(Collectors.toList());
        }

        // Verificamos si la consulta es un número para buscar por ID (código).
        if (query.matches("\\d+")) {
            try {
                Long userId = Long.parseLong(query);
                return userRepository.findById(userId)
                        .filter(user -> user.getRol() != null && user.getRol().equals(clientRole)) // Aseguramos que el ID corresponda a un cliente.
                        .map(List::of) // Convertimos el Optional<User> a una lista.
                        .orElse(List.of()); // Si no se encuentra, devolvemos una lista vacía.
            } catch (NumberFormatException e) {
                return List.of(); // Si no es un Long válido, no es un ID.
            }
        }

        // Si no es un número, realizamos la búsqueda por texto.
        String lowerCaseQuery = query.toLowerCase();
        return userRepository.findAll().stream()
                .filter(user -> user.getRol() != null && user.getRol().equals(clientRole) &&
                        (user.getNombre().toLowerCase().contains(lowerCaseQuery) ||
                        user.getApellido().toLowerCase().contains(lowerCaseQuery) ||
                        user.getCorreo().toLowerCase().contains(lowerCaseQuery) ||
                        (user.getTelefono() != null && user.getTelefono().contains(lowerCaseQuery))))
                .collect(Collectors.toList());
    }
}