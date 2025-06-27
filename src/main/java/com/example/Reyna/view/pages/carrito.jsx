import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Carrito.css';

const CarritoPage = () => {
  // Estado para almacenar los productos del carrito
  const [cartItems, setCartItems] = useState([]);
  const [promoCode, setPromoCode] = useState('');
  const [discount, setDiscount] = useState(0);
  const [subtotal, setSubtotal] = useState(0);
  const [total, setTotal] = useState(0);

  // Cargar los productos del carrito desde localStorage al montar el componente
  useEffect(() => {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      const parsedCart = JSON.parse(storedCart);
      setCartItems(parsedCart);
      calculateTotals(parsedCart);
    }
  }, []);

  // Función para calcular subtotales y totales
  const calculateTotals = (items) => {
    const cartSubtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    setSubtotal(cartSubtotal);
    // Aplicar descuento si existe
    setTotal(cartSubtotal - discount);
  };

  // Función para eliminar un producto del carrito
  const removeItem = (index) => {
    const updatedCart = [...cartItems];
    updatedCart.splice(index, 1);
    setCartItems(updatedCart);
    localStorage.setItem('cart', JSON.stringify(updatedCart));
    calculateTotals(updatedCart);
  };

  // Función para actualizar la cantidad de un producto
  const updateQuantity = (index, quantity) => {
    if (quantity > 0) {
      const updatedCart = [...cartItems];
      updatedCart[index].quantity = quantity;
      setCartItems(updatedCart);
      localStorage.setItem('cart', JSON.stringify(updatedCart));
      calculateTotals(updatedCart);
    }
  };

  // Función para aplicar código promocional
  const applyPromoCode = () => {
    // Aquí implementaríamos la lógica para validar y aplicar un código promocional
    // Por ahora, simulamos un descuento fijo de S/ 10.00 con el código "PROMO10"
    if (promoCode.toUpperCase() === 'PROMO10') {
      const discountAmount = 10;
      setDiscount(discountAmount);
      setTotal(subtotal - discountAmount);
      alert('¡Código promocional aplicado con éxito!');
    } else {
      alert('Código promocional inválido');
      setDiscount(0);
      setTotal(subtotal);
    }
  };

  // Función para formatear precios a formato peruano (S/ XX.XX)
  const formatPrice = (price) => {
    return `S/ ${price.toFixed(2)}`;
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <section className="cesta-header text-center">
        <div className="container">
          <h1>CESTA DE COMPRA</h1>
        </div>
      </section>
      
      <main className="container py-5">
        {cartItems.length > 0 ? (
          <div className="row">
            <div className="col-lg-12">          <div className="tabla-carrito">
                <table className="table">
                  <thead>
                    <tr className="text-center">
                      <th>PRODUCTO</th>
                      <th>PRECIO</th>
                      <th>CANT.</th>
                      <th>CON DESCU.</th>
                      <th>TOTAL</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>                    {cartItems.map((item, index) => (
                      <tr key={index} className="align-middle">
                        <td>
                          <div className="d-flex align-items-center">
                            <img src={item.image} alt={item.name} className="cart-item-image me-3" />
                            <div>
                              <h6>{item.name}</h6>
                              {item.volume && <small>{item.volume}</small>}
                            </div>
                          </div>
                        </td>
                        <td className="text-center">{formatPrice(item.price)}</td>
                        <td className="text-center">
                          <div className="quantity-control">
                            <button onClick={() => updateQuantity(index, item.quantity - 1)} className="btn btn-sm">-</button>
                            <span>{item.quantity}</span>
                            <button onClick={() => updateQuantity(index, item.quantity + 1)} className="btn btn-sm">+</button>
                          </div>
                        </td>
                        <td className="text-center">
                          {item.discountedPrice ? formatPrice(item.discountedPrice) : "S/ 0.00"}
                        </td>
                        <td className="text-center">{formatPrice(item.price * item.quantity)}</td>
                        <td className="text-center">
                          <button onClick={() => removeItem(index)} className="btn btn-sm remove-btn">
                            <i className="bi bi-x"></i>
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>

              <div className="row mt-4">
                <div className="col-md-6">
                  <div className="promo-code-section">
                    <h5>¿Tienes algún código promocional?</h5>
                    <div className="input-group mb-3">
                      <input
                        type="text"
                        className="form-control"
                        placeholder="Ingresa tu código"
                        value={promoCode}
                        onChange={(e) => setPromoCode(e.target.value)}
                      />
                      <button onClick={applyPromoCode} className="btn btn-primary apply-btn">
                        Aplicar
                      </button>
                    </div>
                  </div>
                </div>
                <div className="col-md-6">
                  <div className="cart-totals">
                    <div className="d-flex justify-content-between">
                      <span>SUBTOTAL:</span>
                      <span>{formatPrice(subtotal)}</span>
                    </div>
                    {discount > 0 && (
                      <div className="d-flex justify-content-between text-success">
                        <span>DESCUENTO:</span>
                        <span>-{formatPrice(discount)}</span>
                      </div>
                    )}
                    <div className="d-flex justify-content-between total-line">
                      <span>TOTAL:</span>
                      <span>{formatPrice(total)}</span>
                    </div>                    <div className="text-end mt-3">
                      <Link to="/checkout" className="btn checkout-btn">
                        Comprar
                      </Link>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        ) : (
          <div className="empty-cart text-center py-5">
            <i className="bi bi-cart-x empty-cart-icon"></i>
            <h3>Tu carrito está vacío</h3>
            <p>Parece que aún no has añadido productos a tu carrito.</p>
            <Link to="/productos" className="btn btn-primary">
              Ver Productos
            </Link>
          </div>
        )}
      </main>

      <Footer />
    </div>
  );
};

export default CarritoPage;