package com.example.Reyna.controller;

import com.example.Reyna.model.Boleta;
import com.example.Reyna.dao.BoletaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;
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

    // Buscar boleta por código (id de boleta)
    @GetMapping("/buscar/codigo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Boleta> buscarPorCodigo(@RequestParam Long id) {
        return boletaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Descargar PDF de una boleta específica
    @GetMapping("/pdf/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<byte[]> descargarPdfBoleta(@PathVariable Long id) {
        Optional<Boleta> boletaOptional = boletaRepository.findById(id);
        if (boletaOptional.isPresent()) {
            Boleta boleta = boletaOptional.get();
            if (boleta.getPdf() != null) {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_PDF);
                headers.setContentDispositionFormData("attachment", "boleta-" + boleta.getId() + ".pdf");
                return new ResponseEntity<>(boleta.getPdf(), headers, HttpStatus.OK);
            }
        }
        return ResponseEntity.notFound().build();
    }

    // Guardar solo el archivo PDF en la base de datos
    @PostMapping("/guardar-pdf")
    public ResponseEntity<String> guardarPdf(@RequestParam("pdf") MultipartFile pdfFile) {
        try {
            logger.info("Iniciando el proceso de guardar el PDF");
            Boleta boleta = new Boleta();
            boleta.setPdf(pdfFile.getBytes());
            boleta.setFechaVenta(LocalDateTime.now()); // Establecer la fecha actual
            boletaRepository.save(boleta);
            logger.info("PDF guardado exitosamente con ID: {}", boleta.getId());
            return ResponseEntity.ok("PDF guardado exitosamente");
        } catch (IOException e) {
            logger.error("Error al guardar el PDF: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar el PDF");
        }
    }

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

            // Calcular totales
            double subtotal = productos.stream().mapToDouble(p -> p.getPrecio() * p.getStock()).sum();
            double igv = subtotal * 0.18;
            double total = subtotal + igv;

            // Generar un código único para la boleta
            String codigoBoleta = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear y guardar la boleta
            Boleta boleta = new Boleta();
            boleta.setCodigo(codigoBoleta);
            boleta.setCliente(cliente);
            boleta.setProductos(productos);
            boleta.setSubtotal(subtotal);
            boleta.setIgv(igv);
            boleta.setTotal(total);
            boletaRepository.save(boleta);

            return ResponseEntity.ok("Boleta creada exitosamente con código: " + codigoBoleta);
        } catch (Exception e) {
            logger.error("Error al crear la boleta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la boleta");
        }
    }

    // Guardar la información de la compra como boleta
    @PostMapping("/guardar")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> guardarBoleta(@RequestParam Long clienteId, @RequestBody List<Long> productosIds) {
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

            // Calcular totales
            double subtotal = productos.stream().mapToDouble(p -> p.getPrecio() * p.getStock()).sum();
            double igv = subtotal * 0.18;
            double total = subtotal + igv;

            // Generar un código único para la boleta
            String codigoBoleta = "BOL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear y guardar la boleta
            Boleta boleta = new Boleta();
            boleta.setCodigo(codigoBoleta);
            boleta.setCliente(cliente);
            boleta.setProductos(productos);
            boleta.setSubtotal(subtotal);
            boleta.setIgv(igv);
            boleta.setTotal(total);
            boletaRepository.save(boleta);

            return ResponseEntity.ok("Boleta guardada exitosamente con código: " + codigoBoleta);
        } catch (Exception e) {
            logger.error("Error al guardar la boleta: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la boleta");
        }
    }
}
