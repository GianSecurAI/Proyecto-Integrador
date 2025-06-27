package com.example.Reyna.util;

import com.example.Reyna.model.User;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import com.example.Reyna.model.Producto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ExcelExporter {
    public static ByteArrayInputStream productosToExcel(List<Producto> productos) throws IOException {
    String[] columns = {"ID", "Nombre", "Categoría", "Precio"};
    try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
        Sheet sheet = workbook.createSheet("Productos");
        Row headerRow = sheet.createRow(0);

        for (int col = 0; col < columns.length; col++) {
            Cell cell = headerRow.createCell(col);
            cell.setCellValue(columns[col]);
        }

        int rowIdx = 1;
        for (Producto producto : productos) {
            Row row = sheet.createRow(rowIdx++);
            row.createCell(0).setCellValue(producto.getId_producto());
            row.createCell(1).setCellValue(producto.getNombre_producto());
            row.createCell(2).setCellValue(producto.getCategoria() != null ? producto.getCategoria().getNombre_categoria() : "");
            row.createCell(3).setCellValue(producto.getPrecio());
        }
        workbook.write(out);
        return new ByteArrayInputStream(out.toByteArray());
    }
}

    public static ByteArrayInputStream usersToExcel(List<User> users) throws IOException {
        String[] columns = {"ID", "Nombre Completo", "Correo", "Teléfono", "Dirección"};
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Usuarios");
            Row headerRow = sheet.createRow(0);

            // Crear encabezados
            for (int col = 0; col < columns.length; col++) {
                Cell cell = headerRow.createCell(col);
                cell.setCellValue(columns[col]);
            }

            // Rellenar datos
            int rowIdx = 1;
            for (User user : users) {
                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(user.getId_usuario());
                // Concatenar nombre y apellido para la columna "Nombre Completo"
                row.createCell(1).setCellValue(user.getNombre() + " " + user.getApellido());
                row.createCell(2).setCellValue(user.getCorreo());
                row.createCell(3).setCellValue(user.getTelefono());
                row.createCell(4).setCellValue(user.getDireccion());
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
    
}
