import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Checkout.css';
import apiClient from '../../api/apiClient';
import fondoImg from '../../../../../../../assets/img/fondo.png';

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
  const [deliveryMethod, setDeliveryMethod] = useState('tienda'); // Nuevo estado para método de entrega
  const [deliveryFee, setDeliveryFee] = useState(0); // Cargo por delivery
  const [successModal, setSuccessModal] = useState(false);
  const [loading, setLoading] = useState(true); // Estado para la carga de datos
  
  // Datos del usuario
  const [userData, setUserData] = useState({
    nombre: '',
    apellido: '',
    correo: '',
    telefono: '',
    direccion: ''
  });
  
  // Edición temporal de los datos del usuario
  const [tempUserData, setTempUserData] = useState({ ...userData });

  // Cargar los productos del carrito y los datos del usuario desde el backend al montar el componente
  useEffect(() => {
    const storedCart = localStorage.getItem('cart');
    // Primero, valida el carrito. Si está vacío, no tiene sentido continuar.
    if (!storedCart || JSON.parse(storedCart).length === 0) {
      alert('Tu carrito está vacío. Serás redirigido.');
      navigate('/carrito');
      return; // Detiene la ejecución del efecto
    }

    const parsedCart = JSON.parse(storedCart);
    setCartItems(parsedCart);
    calculateTotals(parsedCart);

    // Obtener datos del usuario autenticado
    const id_usuario = localStorage.getItem('id_usuario');
    if (!id_usuario) {
      setLoading(false); // No hay usuario, finaliza la carga
      alert('Para continuar con la compra, por favor inicie sesión.');
      navigate('/login');
      return; // Detiene la ejecución del efecto
    }
        apiClient(`/api/usuarios/me`).then(async response => {
        if (!response.ok) {
          const errorData = await response.json().catch(() => ({ message: 'Error al leer la respuesta del servidor.' }));
          throw new Error(errorData.message || `Error del servidor: ${response.status}`);
        }
        return response.json();
      })
      .then(user => {
        console.log('Usuario recibido:', user);
        if (user && user.nombre) { // Una validación extra para asegurar que el objeto de usuario es válido
            const currentUserData = {
            nombre: user.nombre || '',
            apellido: user.apellido || '',
            correo: user.correo || '',
            telefono: user.telefono || '',
            direccion: user.direccion || ''
          };
        setUserData(currentUserData);
        setTempUserData(currentUserData);
        } else {
          // Si el usuario no se encuentra o la respuesta no es la esperada
          throw new Error('No se encontraron los datos del usuario.');
        }
      })
      .catch(error => {
        console.error("Error al obtener los datos del usuario:", error);
              alert(`No se pudieron cargar sus datos: ${error.message}. Por favor, inicie sesión de nuevo.`);
      localStorage.removeItem('token');
      localStorage.removeItem('id_usuario');
      navigate('/login');

      })
      .finally(() => {
        setLoading(false);
      });
  }, [navigate]);

  // Función para calcular subtotales y totales
  const calculateTotals = (items) => {
    const cartSubtotal = items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    setSubtotal(cartSubtotal);
    // Aplicar descuento si existe (en este ejemplo el descuento es 0)
    const finalTotal = cartSubtotal - discount + deliveryFee;
    setTotal(finalTotal);
  };

  // Función para manejar el cambio de método de entrega
  const handleDeliveryMethodChange = (method) => {
    setDeliveryMethod(method);
    const newDeliveryFee = method === 'delivery' ? 20 : 0;
    setDeliveryFee(newDeliveryFee);
    
    // Recalcular totales con el nuevo cargo de delivery
    const cartSubtotal = cartItems.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    const finalTotal = cartSubtotal - discount + newDeliveryFee;
    setTotal(finalTotal);
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
  const saveChanges = async () => {
    const id_usuario = localStorage.getItem('id_usuario');
    if (!id_usuario) {
      alert('No se puede guardar, no se ha identificado al usuario.');
      return;
    }

    try {
      // --- IMPORTANTE: CORRECCIÓN DE LA RUTA ---
      // La ruta para actualizar también debe ser la de usuario, no la de admin.
      // Usamos la nueva ruta '/api/usuarios/me'.
      const response = await apiClient(`/api/usuarios/me`, {
        method: 'PUT',
        body: JSON.stringify(tempUserData),
      });

      if (!response.ok) {
        const errorData = await response.json().catch(() => ({ message: 'Error al leer la respuesta del servidor.' }));
        throw new Error(errorData.message || `Error del servidor: ${response.status}`);
      }

      const updatedUser = await response.json();
      setUserData(updatedUser);
      setEditMode(false);
      alert('Información actualizada correctamente.');
    } catch (error) {
      console.error('Error al guardar los cambios:', error);
      alert(`No se pudieron guardar los cambios: ${error.message}`);
    }
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
  const submitOrder = async () => {
    // Simulación de id_cliente (en una app real, obtén el id del usuario autenticado)
     const id_usuario = localStorage.getItem('id_usuario');

    if (!id_usuario) {
      alert('Error: No se ha identificado al usuario. Por favor, inicie sesión de nuevo.');
      // O a la página de inicio de sesión que corresponda
      return;
    }

    // El ID del cliente es el del usuario logueado
    const id_cliente = parseInt(id_usuario, 10);

    // Construir el array de productos para el backend
    const productos = cartItems.map(item => ({
      id_producto: item.id_producto, // Usar id_producto en lugar de id
      cantidad: item.quantity,
      precioUnitario: item.price,
      subtotal: item.price * item.quantity
    }));

    const pedido = {
      id_cliente,
      productos,
      total
    };

    try {
      const response = await apiClient('/api/pedidos', {
        method: 'POST',
        body: JSON.stringify(pedido)
      });
      if (response.ok) {
        setSuccessModal(true);
        localStorage.removeItem('cart');
      } else {
        const errorData = await response.json().catch(() => ({ message: 'Error al leer la respuesta del servidor.' }));
        throw new Error(errorData.message || `Error del servidor: ${response.status}`);
      }
    } catch (error) {
      console.error("Error al enviar el pedido:", error);
      alert(`Error al enviar el pedido: ${error.message}`);
    }
  };
  
  // Cerrar el modal de éxito y redirigir
  const closeSuccessModal = () => {
    setSuccessModal(false);
    navigate('/');
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <section className="checkout-header text-center" style={{ backgroundImage: `url(${fondoImg})`, backgroundSize: 'cover', backgroundPosition: 'center' }}>
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
              
              {loading ? (
                <p>Cargando información del cliente...</p>
              ) : !editMode ? (
                // Modo visualización
                <div>
                  <div className="user-info-field">
                    <span className="user-info-label">Nombre:</span>
                    <div className="user-info-value">{userData.nombre}</div>
                  </div>
                  <div className="user-info-field">
                    <span className="user-info-label">Apellido:</span>
                    <div className="user-info-value">{userData.apellido}</div>
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
                    <label className="user-info-label">Nombre:</label>
                    <input 
                      type="text" 
                      name="nombre" 
                      value={tempUserData.nombre} 
                      onChange={handleInputChange} 
                      className="edit-field" 
                    />
                  </div>
                   <div className="user-info-field">
                    <label className="user-info-label">Apellido:</label>
                    <input 
                      type="text" 
                      name="apellido" 
                      value={tempUserData.apellido} 
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
                    <label className="user-info-label">Dirección:</label>
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
              
              {deliveryFee > 0 && (
                <div className="summary-item">
                  <span>Cargo por delivery:</span>
                  <span>+{formatPrice(deliveryFee)}</span>
                </div>
              )}
              
              <div className="summary-item summary-total">
                <span>Total:</span>
                <span>{formatPrice(total)}</span>
              </div>
              
              {/* Opciones de entrega */}
              <div className="payment-options">
                <h5>Método de entrega:</h5>
                
                <div className="payment-option">
                  <input 
                    type="radio" 
                    id="tienda" 
                    name="deliveryMethod" 
                    value="tienda" 
                    checked={deliveryMethod === 'tienda'} 
                    onChange={() => handleDeliveryMethodChange('tienda')} 
                  />
                  <label htmlFor="tienda">Recojo en tienda</label>
                </div>
                
                <div className="payment-option">
                  <input 
                    type="radio" 
                    id="delivery" 
                    name="deliveryMethod" 
                    value="delivery" 
                    checked={deliveryMethod === 'delivery'} 
                    onChange={() => handleDeliveryMethodChange('delivery')} 
                  />
                  <label htmlFor="delivery">Delivery (+S/ 20.00)</label>
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
              onClick={() => navigate('/pago', { state: { total, userData, cartItems, deliveryMethod, deliveryFee } })}
            >
              Continuar con el pago
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
