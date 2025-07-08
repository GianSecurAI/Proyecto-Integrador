package com.example.Reyna.model;

import com.example.Reyna.dao.CategoriaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class CategoriaRepositoryTest {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Test
    void testGuardarYRecuperarCategoria() {
        // Crear categoría
        Categoria categoria = new Categoria();
        categoria.setNombre_categoria("Electrónica");

        // Guardar
        Categoria guardada = categoriaRepository.save(categoria);

        // Verificar
        assertThat(guardada.getId_categoria()).isNotNull();
        assertThat(guardada.getNombre_categoria()).isEqualTo("Electrónica");
    }
}
