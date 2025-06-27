import React from 'react';
import './App.css';
import RegistrarUsuario from './main/java/com/example/Reyna/view/pages/admin/RegistrarUsuario.jsx';
import Home from './main/java/com/example/Reyna/view/pages/home.jsx';
import ProductsPage from './main/java/com/example/Reyna/view/pages/productos.jsx';
import NosotrosPage from './main/java/com/example/Reyna/view/pages/nosotros.jsx';
import ContactoPage from './main/java/com/example/Reyna/view/pages/contacto.jsx';
import ProductoPage from './main/java/com/example/Reyna/view/pages/productopage.jsx'; // Importar ProductoPage
import Login from './main/java/com/example/Reyna/view/pages/login.jsx'; // Importar Login
import CarritoPage from './main/java/com/example/Reyna/view/pages/carrito.jsx'; // Importar CarritoPage
import Dashboard from './main/java/com/example/Reyna/view/pages/admin/Dashboard.jsx'; // Importar Dashboard
import ProductosAdmin from './main/java/com/example/Reyna/view/pages/admin/ProductosAdmin.jsx'; // Importar ProductosAdmin
import ClientesAdmin from './main/java/com/example/Reyna/view/pages/admin/ClientesAdmin.jsx'; // Importar ClientesAdmin
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

const App = () => {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/productos" element={<ProductsPage />} />
        <Route path="/nosotros" element={<NosotrosPage />} />
        <Route path="/contacto" element={<ContactoPage />} />
        <Route path="/producto/:id" element={<ProductoPage />} /> {/* Ruta dinámica para todos los productos */}
        <Route path="/login" element={<Login />} /> {/* Ruta para Login */}
        <Route path="/carrito" element={<CarritoPage />} /> {/* Ruta para Carrito */}
          {/* Rutas de administración */}
        <Route path="/admin/dashboard" element={<Dashboard />} /> {/* Ruta para el panel de administración */}
        <Route path="/admin/productos" element={<ProductosAdmin />} /> {/* Ruta para la administración de productos */}
        <Route path="/admin/clientes" element={<ClientesAdmin />} /> {/* Ruta para la administración de clientes */}
        <Route path="/admin/registrar-usuario" element={<RegistrarUsuario />} /> {/* Ruta para registrar un nuevo usuario */}
        
      </Routes>
    </Router>
  );
};

export default App;
