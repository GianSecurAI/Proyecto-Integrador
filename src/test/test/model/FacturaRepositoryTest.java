package com.example.Reyna.dao;

import com.example.Reyna.model.Factura;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Rol;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class FacturaRepositoryTest {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testGuardarFactura() {
        // Crear y guardar cliente
        User cliente = new User();
        cliente.setNombre("Juan");
        cliente.setApellido("Pérez");
        cliente.setCorreo("juan@example.com");
        cliente.setContraseña("123");
        cliente.setEstado(true);
        userRepository.save(cliente);

        // Crear y guardar factura
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setRucCliente("20123456789");
        factura.setCodigo("FAC-TEST123");
        factura.setTotal(100.0);
        factura.setSubtotal(84.75);
        factura.setIgv(15.25);
        factura.setFechaFactura(LocalDateTime.now());

        // Guardar y verificar
        Factura guardada = facturaRepository.save(factura);
        assertThat(guardada.getId()).isNotNull();
        assertThat(guardada.getRucCliente()).isEqualTo("20123456789");
        assertThat(guardada.getCodigo()).isEqualTo("FAC-TEST123");
        assertThat(guardada.getCliente().getNombre()).isEqualTo("Juan");
    }

    @Test
    void testBuscarFacturaPorCodigo() {
        // Crear y guardar cliente
        User cliente = new User();
        cliente.setNombre("María");
        cliente.setApellido("González");
        cliente.setCorreo("maria@example.com");
        cliente.setContraseña("123");
        cliente.setEstado(true);
        userRepository.save(cliente);

        // Crear y guardar factura
        Factura factura = new Factura();
        factura.setCliente(cliente);
        factura.setRucCliente("20987654321");
        factura.setCodigo("FAC-SEARCH123");
        factura.setTotal(200.0);
        factura.setSubtotal(169.49);
        factura.setIgv(30.51);
        facturaRepository.save(factura);

        // Buscar por código
        Optional<Factura> encontrada = facturaRepository.findByCodigo("FAC-SEARCH123");
        assertThat(encontrada).isPresent();
        assertThat(encontrada.get().getRucCliente()).isEqualTo("20987654321");
        assertThat(encontrada.get().getCliente().getNombre()).isEqualTo("María");
    }

    @Test
    void testBuscarFacturaPorCodigoNoExistente() {
        Optional<Factura> encontrada = facturaRepository.findByCodigo("FAC-NOEXISTE");
        assertThat(encontrada).isEmpty();
    }
}
