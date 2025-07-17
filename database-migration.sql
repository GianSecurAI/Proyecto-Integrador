-- MIGRACIÓN DE BASE DE DATOS
-- 
-- Descripción: Agregar campos para método de entrega y cargo de delivery

-- ===================================================================
-- PARA LA MAYORÍA DEL EQUIPO: ¡NO HACER NADA!
-- ===================================================================
-- Si tu application.properties tiene "spring.jpa.hibernate.ddl-auto=update"
-- (que es la configuración por defecto del proyecto), entonces:
-- 
-- 1. Haz: git pull
-- 2. Ejecuta la aplicación normalmente
-- 3. Hibernate creará automáticamente las nuevas columnas
-- 
-- ===================================================================
-- SOLO SI tienes problemas o ddl-auto=none, ejecuta estos queries:
-- ===================================================================

-- Para la tabla venta (boletas)
ALTER TABLE venta 
ADD COLUMN metodo_entrega VARCHAR(50),
ADD COLUMN cargo_delivery DECIMAL(10, 2) DEFAULT 0.0;

-- Para la tabla factura 
ALTER TABLE factura 
ADD COLUMN metodo_entrega VARCHAR(50),
ADD COLUMN cargo_delivery DECIMAL(10, 2) DEFAULT 0.0;

-- Verificar que las columnas se agregaron correctamente
-- Ejecutar para verificar estructura de venta:
-- SELECT column_name, data_type, is_nullable, column_default 
-- FROM information_schema.columns 
-- WHERE table_name = 'venta' AND column_name IN ('metodo_entrega', 'cargo_delivery');

-- Ejecutar para verificar estructura de factura:
-- SELECT column_name, data_type, is_nullable, column_default 
-- FROM information_schema.columns 
-- WHERE table_name = 'factura' AND column_name IN ('metodo_entrega', 'cargo_delivery');

-- OPCIONAL: Si quieren limpiar el campo no utilizado (como discutimos antes)
-- ALTER TABLE venta DROP COLUMN pdf_boleta;
