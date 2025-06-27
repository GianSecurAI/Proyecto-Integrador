import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Checkout.css';

const CheckoutPage = () => {
  const navigate = useNavigate();
  const [cartItems, setCartItems] = useState([]);
  const [subtotal, setSubtotal] = useState(0);
  const [discount, setDiscount] = useState(0);
  const [total, setTotal] = useState(0);
  const [editMode, setEditMode] = useState(false);
  const [showTerms, setShowTerms] = useState(false);
  const [termsAccepted, setTermsAccepted] = useState(false);
  const [paymentMethod, setPaymentMethod] = useState('tienda');
  const [successModal, setSuccessModal] = useState(false);
  
  // Datos del usuario
  const [userData, setUserData] = useState({
    nombreCompleto: 'Carlos Rodríguez',
    correo: 'carlos@ejemplo.com',
    telefono: '987654321',
    direccion: 'Av. Principal 123, Lima'
  });
  
  // Edición temporal de los datos del usuario
  const [tempUserData, setTempUserData] = useState({...userData});

  // Cargar los productos del carrito desde localStorage al montar el componente
  useEffect(() => {
    const storedCart = localStorage.getItem('cart');
    if (storedCart) {
      const parsedCart = JSON.parse(storedCart);
      setCartItems(parsedCart);
      calculateTotals(parsedCart);
    } else {
      // Si no hay items en el carrito, redirigir al carrito
      navigate('/carrito');
    }
    
    // En una aplicación real, aquí cargaríamos los datos del usuario desde el backend
    // Por ahora usamos datos de ejemplo
  }, [navigate]);

  // Función para calcular subtotales y totales
  const calculateTotals = (items) => {
    const cartSubtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    setSubtotal(cartSubtotal);
    // Aplicar descuento si existe (en este ejemplo el descuento es 0)
    setTotal(cartSubtotal - discount);
  };

  // Función para formatear precios a formato peruano (S/ XX.XX)
  const formatPrice = (price) => {
    return `S/ ${price.toFixed(2)}`;
  };
  
  // Manejar cambios en los campos de edición
  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setTempUserData({ ...tempUserData, [name]: value });
  };
  
  // Activar modo de edición
  const enableEditMode = () => {
    setTempUserData({...userData});
    setEditMode(true);
  };
  
  // Guardar cambios después de editar
  const saveChanges = () => {
    setUserData({...tempUserData});
    setEditMode(false);
    // En una aplicación real, aquí enviaríamos los cambios al backend
  };
  
  // Abrir el modal de términos y condiciones
  const openTerms = (e) => {
    e.preventDefault();
    setShowTerms(true);
  };
  
  // Cerrar el modal de términos y condiciones
  const closeTerms = () => {
    setShowTerms(false);
  };
  
  // Aceptar términos y condiciones
  const acceptTerms = () => {
    setTermsAccepted(true);
    setShowTerms(false);
  };
  
  // Enviar el pedido
  const submitOrder = () => {
    // En una aplicación real, aquí enviaríamos el pedido al backend
    setSuccessModal(true);
    // Limpiar el carrito
    localStorage.removeItem('cart');
  };
  
  // Cerrar el modal de éxito y redirigir
  const closeSuccessModal = () => {
    setSuccessModal(false);
    navigate('/');
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <section className="checkout-header text-center">
        <div className="container">
          <h1>CONFIRMACIÓN DE COMPRA</h1>
        </div>
      </section>
      
      <main className="container py-5 checkout-container">
        <div className="row">
          {/* Columna izquierda - Información del usuario */}
          <div className="col-lg-6 mb-4">
            <div className="user-info-section">
              <h3 className="user-info-title">Información del Cliente</h3>
              
              {!editMode ? (
                // Modo visualización
                <div>
                  <div className="user-info-field">
                    <span className="user-info-label">Nombre completo:</span>
                    <div className="user-info-value">{userData.nombreCompleto}</div>
                  </div>
                  <div className="user-info-field">
                    <span className="user-info-label">Correo electrónico:</span>
                    <div className="user-info-value">{userData.correo}</div>
                  </div>
                  <div className="user-info-field">
                    <span className="user-info-label">Teléfono:</span>
                    <div className="user-info-value">{userData.telefono}</div>
                  </div>
                  <div className="user-info-field">
                    <span className="user-info-label">Dirección de entrega:</span>
                    <div className="user-info-value">{userData.direccion}</div>
                  </div>
                  
                  <div className="d-flex mt-4">
                    <button className="user-actions-btn edit-btn" onClick={enableEditMode}>
                      Editar información
                    </button>
                  </div>
                </div>
              ) : (
                // Modo edición
                <div>
                  <div className="user-info-field">
                    <label className="user-info-label">Nombre completo:</label>
                    <input 
                      type="text" 
                      name="nombreCompleto" 
                      value={tempUserData.nombreCompleto} 
                      onChange={handleInputChange} 
                      className="edit-field" 
                    />
                  </div>
                  <div className="user-info-field">
                    <label className="user-info-label">Correo electrónico:</label>
                    <input 
                      type="email" 
                      name="correo" 
                      value={tempUserData.correo} 
                      onChange={handleInputChange} 
                      className="edit-field" 
                    />
                  </div>
                  <div className="user-info-field">
                    <label className="user-info-label">Teléfono:</label>
                    <input 
                      type="tel" 
                      name="telefono" 
                      value={tempUserData.telefono} 
                      onChange={handleInputChange} 
                      className="edit-field" 
                    />
                  </div>
                  <div className="user-info-field">
                    <label className="user-info-label">Dirección de entrega:</label>
                    <input 
                      type="text" 
                      name="direccion" 
                      value={tempUserData.direccion} 
                      onChange={handleInputChange} 
                      className="edit-field" 
                    />
                  </div>
                  
                  <div className="d-flex mt-4">
                    <button className="user-actions-btn save-btn" onClick={saveChanges}>
                      Guardar cambios
                    </button>
                    <button className="user-actions-btn edit-btn" onClick={() => setEditMode(false)}>
                      Cancelar
                    </button>
                  </div>
                </div>
              )}
            </div>
          </div>
          
          {/* Columna derecha - Resumen del pedido */}
          <div className="col-lg-6">
            <div className="order-summary">
              <h3 className="order-summary-title">Resumen del Pedido</h3>
              
              {/* Lista de productos resumida */}
              {cartItems.map((item, index) => (
                <div key={index} className="summary-item">
                  <span>{item.name} x {item.quantity}</span>
                  <span>{formatPrice(item.price * item.quantity)}</span>
                </div>
              ))}
              
              {/* Totales */}
              <div className="summary-item">
                <span>Subtotal:</span>
                <span>{formatPrice(subtotal)}</span>
              </div>
              
              {discount > 0 && (
                <div className="summary-item">
                  <span>Descuento:</span>
                  <span>-{formatPrice(discount)}</span>
                </div>
              )}
              
              <div className="summary-item summary-total">
                <span>Total:</span>
                <span>{formatPrice(total)}</span>
              </div>
              
              {/* Opciones de pago */}
              <div className="payment-options">
                <h5>Método de pago:</h5>
                
                <div className="payment-option">
                  <input 
                    type="radio" 
                    id="tienda" 
                    name="paymentMethod" 
                    value="tienda" 
                    checked={paymentMethod === 'tienda'} 
                    onChange={() => setPaymentMethod('tienda')} 
                  />
                  <label htmlFor="tienda">Pago en tienda</label>
                </div>
                
                <div className="payment-option">
                  <input 
                    type="radio" 
                    id="contraentrega" 
                    name="paymentMethod" 
                    value="contraentrega" 
                    checked={paymentMethod === 'contraentrega'} 
                    onChange={() => setPaymentMethod('contraentrega')} 
                  />
                  <label htmlFor="contraentrega">Pago contraentrega</label>
                </div>
              </div>
            </div>
            
            {/* Términos y condiciones */}
            <div className="terms-section">
              <label>
                <input 
                  type="checkbox" 
                  checked={termsAccepted} 
                  onChange={() => setTermsAccepted(!termsAccepted)} 
                />
                <span>
                  Acepto los <a href="#terms" onClick={openTerms}>términos y condiciones</a> de compra
                </span>
              </label>
            </div>
            
            {/* Botón para enviar el pedido */}
            <button 
              className="submit-order-btn" 
              disabled={!termsAccepted}
              onClick={submitOrder}
            >
              Enviar Pedido
            </button>
          </div>
        </div>
      </main>
      
      {/* Modal de términos y condiciones */}
      {showTerms && (
        <div className="terms-modal">
          <div className="terms-modal-content">
            <div className="terms-modal-header">
              <h3 className="terms-modal-title">Términos y Condiciones</h3>
              <button className="terms-modal-close" onClick={closeTerms}>&times;</button>
            </div>
            <div className="terms-modal-body">
              <h4>1. Condiciones Generales</h4>
              <p>Las presentes condiciones generales de venta regulan la relación contractual entre La Reyna y el cliente, con respecto a los pedidos de productos realizados a través de nuestra tienda online.</p>
              
              <h4>2. Productos y Precios</h4>
              <p>Las características de los productos se describen en cada ficha de producto. Los precios mostrados incluyen el IVA y son expresados en la moneda local (Soles).</p>
              
              <h4>3. Proceso de Compra</h4>
              <p>Para realizar un pedido, el cliente debe seleccionar los productos deseados y seguir el proceso de compra, proporcionando los datos solicitados de forma veraz y completa.</p>
              
              <h4>4. Formas de Pago</h4>
              <p>El cliente puede elegir entre pago en tienda o pago contra entrega. La Reyna se reserva el derecho de rechazar pedidos en caso de datos incorrectos o problemas con el método de pago.</p>
              
              <h4>5. Envíos</h4>
              <p>La Reyna se compromete a entregar los productos en el domicilio indicado por el cliente, en el plazo establecido. Los gastos de envío serán informados antes de finalizar la compra.</p>
              
              <h4>6. Devoluciones</h4>
              <p>El cliente dispone de 14 días naturales desde la recepción para devolver un producto. Los productos devueltos deben estar en perfecto estado y con su embalaje original.</p>
              
              <h4>7. Protección de Datos</h4>
              <p>La información proporcionada por el cliente será tratada conforme a la normativa de protección de datos vigente.</p>
            </div>
            <div className="terms-modal-footer">
              <button className="user-actions-btn save-btn" onClick={acceptTerms}>
                Aceptar
              </button>
            </div>
          </div>
        </div>
      )}
      
      {/* Modal de éxito */}
      {successModal && (
        <div className="success-modal">
          <div className="success-modal-content">
            <div className="success-icon">✓</div>
            <h3 className="success-title">¡Su orden ha sido enviada!</h3>
            <p className="success-message">
              Su pedido ha sido enviado a la tienda. En breve será validada y se le notificará.
              <br /><br />
              Gracias por comprar en La Reyna.
            </p>
            <button className="success-button" onClick={closeSuccessModal}>
              Aceptar
            </button>
          </div>
        </div>
      )}

      <Footer />
    </div>
  );
};

export default CheckoutPage;
