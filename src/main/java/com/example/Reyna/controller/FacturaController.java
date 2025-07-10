package com.example.Reyna.controller;

import com.example.Reyna.model.Factura;
import com.example.Reyna.model.DetalleFactura;
import com.example.Reyna.dao.FacturaRepository;
import com.example.Reyna.dao.DetalleFacturaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import org.springframework.http.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.Reyna.model.User;
import com.example.Reyna.model.Producto;
import com.example.Reyna.dao.UserRepository;
import com.example.Reyna.dao.ProductoRepository;


@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private static final Logger logger = LoggerFactory.getLogger(FacturaController.class);

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private DetalleFacturaRepository detalleFacturaRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductoRepository productoRepository;

    // Buscar factura por código (string)
    @GetMapping("/buscar/codigo")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Factura> buscarPorCodigo(@RequestParam String codigo) {
        return facturaRepository.findByCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Buscar factura por ID
    @GetMapping("/buscar/id")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Factura> buscarPorId(@RequestParam Long id) {
        return facturaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Descargar PDF de una factura específica (funcionalidad removida - no almacenamos PDFs)
    @GetMapping("/pdf/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> descargarPdfFactura(@PathVariable Long id) {
        Optional<Factura> facturaOptional = facturaRepository.findById(id);
        if (facturaOptional.isPresent()) {
            return ResponseEntity.ok("PDF generation not implemented - use frontend to generate PDF with factura data");
        }
        return ResponseEntity.notFound().build();
    }

    // Crear factura con datos de cliente y productos
    @PostMapping("/crear")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> crearFactura(@RequestParam Long clienteId, @RequestParam String rucCliente, @RequestBody List<Long> productosIds) {
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

            // Generar un código único para la factura
            String codigoFactura = "FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();

            // Crear y guardar la factura
            Factura factura = new Factura();
            factura.setCodigo(codigoFactura);
            factura.setCliente(cliente);
            factura.setRucCliente(rucCliente);
            factura.setTotal(total);
            factura.calcularTotales(); // Calcula subtotal e IGV automáticamente
            facturaRepository.save(factura);

            return ResponseEntity.ok("Factura creada exitosamente con código: " + codigoFactura);
        } catch (Exception e) {
            logger.error("Error al crear la factura: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al crear la factura");
        }
    }

    // Guardar la información de la compra como factura
    @PostMapping("/guardar")
    public ResponseEntity<String> guardarFactura(@RequestParam Long clienteId, @RequestParam String rucCliente, @RequestBody List<Long> productosIds) {
        try {
            logger.info("Iniciando guardado de factura para cliente ID: {} con RUC: {} y productos: {}", clienteId, rucCliente, productosIds);
            
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

            // Generar un código único para la factura
            String codigoFactura = "FAC-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
            logger.info("Código generado: {}", codigoFactura);

            // Crear y guardar la factura
            Factura factura = new Factura();
            factura.setCodigo(codigoFactura);
            factura.setCliente(cliente);
            factura.setRucCliente(rucCliente);
            factura.setIdVendedor(vendedorPorDefecto.getId_usuario()); // Asignar vendedor explícitamente
            factura.setTotal(total);
            factura.calcularTotales(); // Calcula subtotal e IGV automáticamente
            
            logger.info("Antes de guardar - Cliente ID: {}, RUC: {}, Vendedor ID: {}, Total: {}, Subtotal: {}, IGV: {}", 
                factura.getCliente().getId_usuario(), factura.getRucCliente(), factura.getIdVendedor(), 
                factura.getTotal(), factura.getSubtotal(), factura.getIgv());
            
            factura = facturaRepository.save(factura);
            logger.info("Factura guardada exitosamente con ID: {}", factura.getId());

            // Crear y guardar los detalles de factura
            for (Producto producto : productos) {
                DetalleFactura detalle = new DetalleFactura();
                detalle.setFactura(factura);
                detalle.setProducto(producto);
                detalle.setCantidad(1); // Por ahora asumimos cantidad 1, se puede mejorar después
                detalle.setPrecioUnitario(BigDecimal.valueOf(producto.getPrecio()));
                detalle.setSubtotal(BigDecimal.valueOf(producto.getPrecio()));
                
                detalleFacturaRepository.save(detalle);
                logger.info("Detalle de factura guardado: Producto ID={}, Cantidad={}, Precio={}", 
                    producto.getId_producto(), detalle.getCantidad(), detalle.getPrecioUnitario());
            }

            String codigoCompleto = "Factura guardada exitosamente con código: " + codigoFactura;
            logger.info(codigoCompleto);
            return ResponseEntity.ok(codigoFactura); // Retornamos solo el código
        } catch (Exception e) {
            logger.error("Error al guardar la factura: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al guardar la factura: " + e.getMessage());
        }
    }
}
