package com.example.Reyna.util;

import com.example.Reyna.model.Categoria;
import com.example.Reyna.model.Producto;
import com.example.Reyna.model.Rol;
import com.example.Reyna.model.User;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ExcelExporterTest {

    @Test
    void testProductosToExcel() throws Exception {
        // Crear productos
        Categoria categoria = new Categoria();
        categoria.setId_categoria(1L);
        categoria.setNombre_categoria("Bebidas");

        Producto producto = new Producto();
        producto.setId_producto(1L);
        producto.setNombre_producto("Coca Cola");
        producto.setCategoria(categoria);
        producto.setPrecio(3.5);

        List<Producto> productos = new ArrayList<>();
        productos.add(producto);

        ByteArrayInputStream excelStream = ExcelExporter.productosToExcel(productos);

        assertNotNull(excelStream);

        Workbook workbook = WorkbookFactory.create(excelStream);
        assertEquals("Productos", workbook.getSheetAt(0).getSheetName());
        assertEquals("Coca Cola", workbook.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());

        workbook.close();
    }

    @Test
    void testUsersToExcel() throws Exception {
        Rol rol = new Rol();
        rol.setId_rol(1L);
        rol.setNombre("CLIENTE");

        User user = User.builder()
                .id_usuario(1L)
                .nombre("Juan")
                .apellido("Pérez")
                .correo("juan@example.com")
                .telefono("123456789")
                .direccion("Av. Lima 123")
                .rol(rol)
                .estado(true)
                .build();

        List<User> users = new ArrayList<>();
        users.add(user);

        ByteArrayInputStream excelStream = ExcelExporter.usersToExcel(users);

        assertNotNull(excelStream);

        Workbook workbook = WorkbookFactory.create(excelStream);
        assertEquals("Usuarios", workbook.getSheetAt(0).getSheetName());
        assertEquals("Juan Pérez", workbook.getSheetAt(0).getRow(1).getCell(1).getStringCellValue());
        assertEquals("juan@example.com", workbook.getSheetAt(0).getRow(1).getCell(2).getStringCellValue());

        workbook.close();
    }
}
