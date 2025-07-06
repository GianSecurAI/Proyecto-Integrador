import React, { useState, useEffect, useRef } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import tituloLaReyna from '../../../../../../../assets/img/titulo-lareyna.png';
import 'bootstrap-icons/font/bootstrap-icons.css';
import '../styles/Navbar.css'; 

const Navbar = () => {
  const [user, setUser] = useState(null);
  const [showDropdown, setShowDropdown] = useState(false);
  const [cartCount, setCartCount] = useState(0);
  const [showCart, setShowCart] = useState(false);
  const dropdownRef = useRef(null);
  const cartPanelRef = useRef(null);

  useEffect(() => {
  const storedUser = localStorage.getItem('user');
  if (storedUser && storedUser !== "undefined") {
    setUser(JSON.parse(storedUser));
  } else {
    setUser(null);
  }
    
    // Actualizar la cuenta del carrito al cargar
    updateCartCount();
    
    // Escuchar los eventos de actualización del carrito
    window.addEventListener('cartUpdated', updateCartCount);
    
    return () => {
      window.removeEventListener('cartUpdated', updateCartCount);
    };
  }, []);

  // Actualizar contador de productos en el carrito
  const updateCartCount = () => {
    const cart = JSON.parse(localStorage.getItem('cart')) || [];
    const count = cart.reduce((total, item) => total + item.quantity, 0);
    setCartCount(count);
  };

  // Cerrar el menú y el panel del carrito al hacer clic fuera
  useEffect(() => {
    function handleClickOutside(event) {
      if (dropdownRef.current && !dropdownRef.current.contains(event.target)) {
        setShowDropdown(false);
      }
      if (cartPanelRef.current && !cartPanelRef.current.contains(event.target)) {
        setShowCart(false);
      }
    }
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const handleLogout = () => {
    localStorage.removeItem('user');
    setUser(null);
    setShowDropdown(false);
    window.location.href = '/login';
  };

  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-white border-bottom fixed-top">
      <div className="container">
        <Link className="navbar-brand fw-bold" to="/">
          <img src={tituloLaReyna} alt="La Reyna" />
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNav">
          <ul className="navbar-nav mx-auto">
            <li className="nav-item"><Link className="nav-link" to="/">INICIO</Link></li>
            <li className="nav-item"><Link className="nav-link" to="/productos">PRODUCTOS</Link></li>
            <li className="nav-item"><Link className="nav-link" to="/nosotros">NOSOTROS</Link></li>
            <li className="nav-item"><Link className="nav-link" to="/contacto">CONTACTO</Link></li>          
             {/* Solo para ADMINISTRADOR */}
             {user && user.role === "ADMINISTRADOR" && (
            <li className="nav-item"><Link className="nav-link" to="/admin/registrar-usuario">REGISTRAR USUARIO</Link></li>
            )}  

            {/* Solo para ADMINISTRADOR y VENDEDOR */}
            {user && (user.role === "ADMINISTRADOR" || user.role === "VENDEDOR") && (
            <li className="nav-item"><Link className="nav-link" to="/ventas">VENTAS</Link></li>
            )}

            {/* Solo para CLIENTE */}
            {user && user.role === "CLIENTE" && (
            <li className="nav-item"><Link className="nav-link" to="/carrito">CARRITO</Link></li>
            )}
            </ul>

           <div className="d-flex align-items-center">
            {/* Icono del carrito */}
            <span 
              className="me-3 text-decoration-none navbar-link cart-icon-container" 
              aria-label="Cesta" 
              style={{ cursor: 'pointer', position: 'relative' }}
              onClick={() => setShowCart(!showCart)}
            >
              <i className="bi bi-bag"></i> CESTA
              {cartCount > 0 && (
                <span className="cart-count">{cartCount}</span>
              )}
            </span>
            
            {/* Panel lateral del carrito */}
            {showCart && (
              <div className="cart-panel" ref={cartPanelRef}>
                <div className="cart-panel-header">
                  <h5>CESTA DE COMPRA</h5>
                  <button className="close-cart-btn" onClick={(e) => {e.stopPropagation(); setShowCart(false)}}>
                    <i className="bi bi-x-lg"></i>
                  </button>
                </div>
                
                <div className="cart-panel-body">
                  {cartCount > 0 ? (
                    <>
                      <div className="cart-items">
                        {(JSON.parse(localStorage.getItem('cart')) || []).map((item, index) => (
                          <div key={index} className="cart-item">
                            <div className="cart-item-image">
                              <img src={item.image} alt={item.name} />
                            </div>
                            <div className="cart-item-details">
                              <h6>{item.name}</h6>
                              <p>Cantidad: {item.quantity}</p>
                              <p>Precio: S/ {item.price.toFixed(2)}</p>
                            </div>
                          </div>
                        ))}
                      </div>
                      <div className="cart-panel-footer">
                        <Link
                          to="/carrito"
                          className="btn w-100 btn-cart-panel"
                          onClick={() => setShowCart(false)}
                        >
                          IR AL CARRITO
                        </Link>
                      </div>
                    </>
                  ) : (
                    <div className="empty-cart-message">
                      <p>Tu carrito está vacío</p>
                      <Link to="/productos" className="btn btn-outline-primary" onClick={() => setShowCart(false)}>
                        Ver Productos
                      </Link>
                    </div>
                  )}
                </div>
              </div>
            )}
            
            {/* Menú de usuario */}
            <div ref={dropdownRef} style={{ position: 'relative', display: 'inline-block' }}>
              {user ? (
                <span
                  className="text-decoration-none navbar-link user-dropdown"
                  style={{ cursor: 'pointer' }}
                  onClick={() => setShowDropdown((prev) => !prev)}
                >
                  <i className="bi bi-person"></i> {user.name}
                  <i className="bi bi-caret-down-fill ms-1" style={{ fontSize: '0.7em' }}></i>
                </span>
              ) : (
                <Link to="/login" className="text-decoration-none navbar-link" aria-label="Cuenta">
                  <i className="bi bi-person"></i> CUENTA
                </Link>
              )}
              {user && showDropdown && (
                <div className="user-dropdown-menu" style={{ position: 'absolute', right: 0, top: '100%', background: '#fff', border: '1px solid #ddd', borderRadius: 4, minWidth: 120, zIndex: 1000 }}>
                   {/* Solo para ADMINISTRADOR */}
                   {user.role === "ADMINISTRADOR" && (
                   <Link to="/admin/registrar-usuario" className="dropdown-item w-100 text-start" style={{ padding: '8px 16px' }}>
                  Registrar Usuario
                  </Link>
                  )}
                  <button className="dropdown-item w-100 text-start" onClick={handleLogout} style={{ background: 'none', border: 'none', padding: '8px 16px', cursor: 'pointer' }}>
                    Salir
                  </button>
                </div>
              )}
            </div>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;