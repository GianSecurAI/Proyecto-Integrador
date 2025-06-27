package com.example.Reyna.Service;

import com.example.Reyna.dao.RolRepository;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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

     public User updateUser(Long id, User userDetails) {
         User existingUser = userRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));

        existingUser.setNombre(userDetails.getNombre()); // Actualizar nombre
        existingUser.setApellido(userDetails.getApellido()); // Actualizar apellido
        existingUser.setCorreo(userDetails.getCorreo());
        // Solo actualiza la contraseña si se proporciona y no está vacía
        if (userDetails.getContraseña() != null && !userDetails.getContraseña().isEmpty()) {
            existingUser.setContraseña(passwordEncoder.encode(userDetails.getContraseña()));
        }
        existingUser.setTelefono(userDetails.getTelefono()); // Corregido: No hay isEnabled() en User
        existingUser.setDireccion(userDetails.getDireccion());
        existingUser.setEstado(userDetails.isEnabled());
        // Opcionalmente, actualiza el rol si userDetails lo contiene y está permitido
        if (userDetails.getRol() != null && userDetails.getRol().getId_rol() != null) {
            Rol newRol = rolRepository.findById(userDetails.getRol().getId_rol())
            .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
            existingUser.setRol(newRol);
        }

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
        return userRepository.findAll().stream()
                .filter(user -> user.getRol() != null && user.getRol().equals(clientRole) &&
                        (query == null || query.trim().isEmpty() ||
                        user.getNombre().toLowerCase().contains(query.toLowerCase()) || // Buscar por nombre
                        user.getApellido().toLowerCase().contains(query.toLowerCase()) || // Buscar por apellido
                         user.getCorreo().toLowerCase().contains(query.toLowerCase()) ||
                         (user.getTelefono() != null && user.getTelefono().toLowerCase().contains(query.toLowerCase()))))
                .collect(Collectors.toList());
    }
}