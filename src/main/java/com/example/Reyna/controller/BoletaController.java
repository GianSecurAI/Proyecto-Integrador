package com.example.Reyna.controller;

import com.example.Reyna.model.Boleta;
import com.example.Reyna.dao.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Producto;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;


@RestController
@RequestMapping("/api/boletas")
public class BoletaController {

    private static final Logger logger = LoggerFactory.getLogger(BoletaController.class);

    @Autowired
    private BoletaRepository boletaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Buscar boleta por código (string)
    @GetMapping("/buscar/codigo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boleta> buscarPorCodigo(@RequestParam String codigo) {
        return boletaRepository.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar boleta por ID
    @GetMapping("/buscar/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boleta> buscarPorId(@RequestParam Long id) {
        return boletaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Descargar PDF de una boleta específica (funcionalidad removida - no almacenamos PDFs)
    @GetMapping("/pdf/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> descargarPdfBoleta(@PathVariable Long id) {
        Optional<Boleta> boletaOptional = boletaRepository.findById(id);
        if (boletaOptional.isPresent()) {
            return ResponseEntity.ok("PDF generation not implemented - use frontend to generate PDF with boleta data");
        }
        return ResponseEntity.notFound().build();
    }

    // Endpoint removido - no guardamos PDFs en la base de datos
    // @PostMapping("/guardar-pdf") - REMOVIDO

    // Crear boleta con datos de cliente y productos
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearBoleta(@RequestParam Long clienteId, @RequestBody List<Long> productosIds) {
        try {
            // Obtener datos del cliente
            Optional<User> clienteOptional = userRepository.findById(clienteId);
            if (clienteOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }
            User cliente = clienteOptional.get();

            // Obtener detalles de los productos
            List<Producto> productos = productoRepository.findAllById(productosIds);
            if (productos.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Productos no encontrados");
            }

            // Calcular total basado en los productos
            double total = productos.stream().mapToDouble(Producto::getPrecio).sum();

            // Generar un código único para la boleta
            String codigoBoleta = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear y guardar la boleta
            Boleta boleta = new Boleta();
            boleta.setCodigo(codigoBoleta);
            boleta.setCliente(cliente);
            boleta.setTotal(total);
            boleta.calcularTotales(); // Calcula subtotal e IGV automáticamente
            boletaRepository.save(boleta);

            return ResponseEntity.ok("Boleta creada exitosamente con código: " + codigoBoleta);
        } catch (Exception e) {
            logger.error("Error al crear la boleta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la boleta");
        }
    }

    // Guardar la información de la compra como boleta
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarBoleta(@RequestParam Long clienteId, @RequestBody List<Long> productosIds) {
        try {
            logger.info("Iniciando guardado de boleta para cliente ID: {} con productos: {}", clienteId, productosIds);
            
            // Verificar que existe al menos un usuario con rol ADMIN o VENDEDOR para asignar como vendedor
            List<User> vendedores = userRepository.findAll().stream()
                .filter(u -> u.getRol() != null && 
                    (u.getRol().getNombre().equals("ADMINISTRADOR") || u.getRol().getNombre().equals("VENDEDOR")))
                .toList();
            
            if (vendedores.isEmpty()) {
                logger.error("No se encontraron vendedores disponibles");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error: No hay vendedores disponibles en el sistema");
            }
            
            User vendedorPorDefecto = vendedores.get(0); // Usar el primer vendedor/admin encontrado
            logger.info("Vendedor por defecto asignado: ID={}, Nombre={}", 
                vendedorPorDefecto.getId_usuario(), vendedorPorDefecto.getNombre());
            
            // Obtener datos del cliente
            Optional<User> clienteOptional = userRepository.findById(clienteId);
            if (clienteOptional.isEmpty()) {
                logger.error("Cliente no encontrado con ID: {}", clienteId);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado");
            }
            User cliente = clienteOptional.get();
            logger.info("Cliente encontrado: {}", cliente.getCorreo());

            // Obtener detalles de los productos
            logger.info("Buscando productos con IDs: {}", productosIds);
            List<Producto> productos = productoRepository.findAllById(productosIds);
            logger.info("Productos encontrados: {} de {} solicitados", productos.size(), productosIds.size());
            
            if (productos.isEmpty()) {
                logger.error("Productos no encontrados para IDs: {}", productosIds);
                // Verificar si hay productos en la base de datos
                long totalProductos = productoRepository.count();
                logger.info("Total de productos en la base de datos: {}", totalProductos);
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Productos no encontrados");
            }
            
            // Log de cada producto encontrado
            productos.forEach(p -> logger.info("Producto encontrado: ID={}, Nombre={}, Precio={}", 
                p.getId_producto(), p.getNombre_producto(), p.getPrecio()));

            // Calcular total basado en los productos
            double total = productos.stream().mapToDouble(Producto::getPrecio).sum();
            logger.info("Total calculado: {}", total);

            // Generar un código único para la boleta
            String codigoBoleta = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            logger.info("Código generado: {}", codigoBoleta);

            // Crear y guardar la boleta
            Boleta boleta = new Boleta();
            boleta.setCodigo(codigoBoleta);
            boleta.setCliente(cliente);
            boleta.setIdVendedor(vendedorPorDefecto.getId_usuario()); // Asignar vendedor explícitamente
            boleta.setTotal(total);
            boleta.calcularTotales(); // Calcula subtotal e IGV automáticamente
            
            logger.info("Antes de guardar - Cliente ID: {}, Vendedor ID: {}, Total: {}, Subtotal: {}, IGV: {}", 
                boleta.getCliente().getId_usuario(), boleta.getIdVendedor(), 
                boleta.getTotal(), boleta.getSubtotal(), boleta.getIgv());
            
            boleta = boletaRepository.save(boleta);
            logger.info("Boleta guardada exitosamente con ID: {}", boleta.getId());

            return ResponseEntity.ok(codigoBoleta); // Retornamos solo el código
        } catch (Exception e) {
            logger.error("Error al guardar la boleta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la boleta: " + e.getMessage());
        }
    }
}
