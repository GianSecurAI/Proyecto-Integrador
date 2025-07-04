create database db_reyna;
-- Tabla de roles
CREATE TABLE Rol (
    id_rol BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(50) NOT NULL
);

-- Tabla de usuarios
CREATE TABLE Usuario (
    id_usuario BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    apellido VARCHAR(100) NOT NULL,
    correo VARCHAR(100) NOT NULL UNIQUE,
    contraseña VARCHAR(255) NOT NULL,
    telefono VARCHAR(20),
    direccion VARCHAR(200),
    id_rol BIGINT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_rol) REFERENCES Rol(id_rol)
);

-- Tabla de categorías de productos
CREATE TABLE Categoria (
    id_categoria BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_categoria VARCHAR(100) NOT NULL
);

-- Tabla de productos
CREATE TABLE Producto (
    id_producto BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    codigo VARCHAR(50) NOT NULL UNIQUE,
    marca VARCHAR(100),
    contenido VARCHAR(100),
    descripcion TEXT,
    precio DECIMAL(10,2) NOT NULL,
    stock INT NOT NULL,
    id_categoria BIGINT NOT NULL,
    estado BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (id_categoria) REFERENCES Categoria(id_categoria)
);

-- Tabla de proveedores
CREATE TABLE Proveedor (
    id_proveedor BIGINT AUTO_INCREMENT PRIMARY KEY,
    razon_social VARCHAR(150) NOT NULL,
    ruc VARCHAR(15) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    correo VARCHAR(100)
);

-- Relación muchos a muchos entre productos y proveedores
CREATE TABLE Producto_Proveedor (
    id_producto BIGINT,
    id_proveedor BIGINT,
    PRIMARY KEY (id_producto, id_proveedor),
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto),
    FOREIGN KEY (id_proveedor) REFERENCES Proveedor(id_proveedor)
);

-- Pedidos realizados por clientes
CREATE TABLE Pedido (
    id_pedido BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    fecha_pedido DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_pedido VARCHAR(50) DEFAULT 'Pendiente',
    total DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES Usuario(id_usuario)
);

-- Detalles del pedido
CREATE TABLE Detalle_Pedido (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_pedido BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_pedido) REFERENCES Pedido(id_pedido),
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

-- Ventas realizadas
CREATE TABLE Venta (
    id_venta BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_vendedor BIGINT NOT NULL,
    id_cliente BIGINT NOT NULL,
    fecha_venta DATETIME DEFAULT CURRENT_TIMESTAMP,
    total DECIMAL(10,2) NOT NULL,
    tipo_comprobante VARCHAR(50),
    FOREIGN KEY (id_vendedor) REFERENCES Usuario(id_usuario),
    FOREIGN KEY (id_cliente) REFERENCES Usuario(id_usuario)
);

-- Detalles de cada venta
CREATE TABLE Detalle_Venta (
    id_detalle BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_venta BIGINT NOT NULL,
    id_producto BIGINT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES Venta(id_venta),
    FOREIGN KEY (id_producto) REFERENCES Producto(id_producto)
);

-- Tabla de estados (opcional)
CREATE TABLE Estado (
    id_estado INT AUTO_INCREMENT PRIMARY KEY,
    nombre_estado VARCHAR(50) NOT NULL
);

-- Contactos o reclamos de clientes
CREATE TABLE Contacto (
    id_contacto BIGINT AUTO_INCREMENT PRIMARY KEY,
    id_cliente BIGINT NOT NULL,
    asunto VARCHAR(100),
    mensaje TEXT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    estado_respuesta VARCHAR(20) DEFAULT 'Pendiente',
    FOREIGN KEY (id_cliente) REFERENCES Usuario(id_usuario)
);

-- Inserción de categorías de productos
INSERT INTO Categoria (nombre_categoria) VALUES
('Damas'),
('Caballeros'),
('Niños'),
('Unisex');

-- Inserción de productos con atributos completos
INSERT INTO Producto (nombre_producto, codigo, marca, contenido, descripcion, precio, stock, id_categoria, estado) VALUES
('Perfume Dama Floral', 'P001', 'Dior', '100ml', 'Perfume floral para dama.', 120.00, 10, 1, TRUE),
('Colonia Caballero Sport', 'P002', 'Hugo Boss', '150ml', 'Colonia fresca para caballero.', 95.00, 15, 2, TRUE),
('Splash Niños Dulce', 'P003', 'Disney', '80ml', 'Splash suave para niños.', 60.00, 20, 3, TRUE),
('Fragancia Unisex Citrus', 'P004', 'CK', '120ml', 'Fragancia cítrica unisex.', 110.00, 8, 4, FALSE);

-- Inserción de estados del catálogo
INSERT INTO Estado (nombre_estado) VALUES
('Disponible'),
('No Disponible');

-- Inserción de roles de usuario
INSERT INTO Rol (nombre) VALUES
('ADMINISTRADOR'),
('VENDEDOR'),
('CLIENTE');

