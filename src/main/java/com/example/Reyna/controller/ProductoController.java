package com.example.Reyna.controller;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.example.Reyna.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.Reyna.model.Producto;
import com.example.Reyna.Service.ProductoService;
import com.example.Reyna.util.ExcelExporter;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @GetMapping
    public List<Producto> getAllProductos() {
        return productoService.obtenerTodos();
    }
    @PostMapping
        public Producto crearProducto(@AuthenticationPrincipal User user, @RequestBody Producto producto) {
        System.out.println("Usuario autenticado: " + user.getCorreo() + ", Rol: " + user.getRol().getNombre());
        return productoService.guardarProducto(producto);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        Producto productoActualizado = productoService.actualizarProducto(id, producto);
        return ResponseEntity.ok(productoActualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        productoService.eliminarProducto(id);
        return ResponseEntity.noContent().build();
    }
    @Autowired
    private ProductoService productoService;

    @GetMapping("/excel")
    public ResponseEntity<byte[]> exportarProductosExcel() throws IOException {
        List<Producto> productos = productoService.obtenerTodos(); // Ajusta según tu servicio
        ByteArrayInputStream in = ExcelExporter.productosToExcel(productos);
        byte[] bytes = in.readAllBytes();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=productos.xlsx");

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(bytes);
    }
    @GetMapping("/admin/solo-admin")
    public ResponseEntity<String> soloParaAdmin(@AuthenticationPrincipal User user) {
      // Corregido: Verificar el rol a través del objeto Rol
    if (user.getRol() != null && "ADMINISTRADOR".equals(user.getRol().getNombre())) {
        return ResponseEntity.ok("¡Bienvenido, administrador!");
    } else {
        return ResponseEntity.status(403).body("Acceso denegado");
    }

}
}