import React, { useState } from 'react';
import PropTypes from 'prop-types';
import { Link } from 'react-router-dom';
import '../styles/ProductDetail.css'; 

const ProductDetail = ({ image, title, volume, price, description, ingredients, id_producto }) => {
  const [quantity, setQuantity] = useState(1);
  const [addedToCart, setAddedToCart] = useState(false);

  const handleQuantityChange = (delta) => {
    setQuantity((prev) => Math.max(1, prev + delta));
  };
  
  const addToCart = () => {
    // Convertir el precio de formato "S/ XX.XX" a número
    const numericPrice = parseFloat(price.replace('S/ ', ''));
    
    // Crear el objeto de producto para el carrito
    const productToAdd = {
      id_producto: id_producto, // Agregar el ID del producto
      image,
      name: title,
      volume,
      price: numericPrice,
      quantity,
      discountedPrice: null // Inicializa sin descuento, podrá aplicarse después
    };
    
    // Obtener el carrito actual o iniciar uno vacío
    let currentCart = JSON.parse(localStorage.getItem('cart')) || [];
    
    // Verificar si el producto ya está en el carrito
    const existingProductIndex = currentCart.findIndex(
      item => item.id_producto === id_producto
    );
    
    if (existingProductIndex !== -1) {
      // Si el producto ya existe, actualizar la cantidad
      currentCart[existingProductIndex].quantity += quantity;
    } else {
      // Si no existe, añadirlo al carrito
      currentCart.push(productToAdd);
    }
    
    // Guardar en localStorage
    localStorage.setItem('cart', JSON.stringify(currentCart));
    
    // Actualizar el estado para mostrar un mensaje
    setAddedToCart(true);
    
    // Desvanecer el mensaje después de 3 segundos
    setTimeout(() => {
      setAddedToCart(false);
    }, 3000);
    
    // Disparar un evento para actualizar el contador del carrito
    window.dispatchEvent(new Event('cartUpdated'));
  };

  return (
    <main className="container py-5">
      <Link to="/productos" className="volver-atras">
        <i className="bi bi-arrow-left"></i> VOLVER ATRÁS
      </Link>
      <div className="row align-items-start g-5">
        {/* Imagen del Producto */}
        <div className="col-md-5 text-center">
          <img src={image} alt={title} className="producto-detalle-img shadow-sm rounded" />
        </div>

        {/* Detalles del Producto */}
        <div className="col-md-7">
          <div className="detalle-titulo mb-1">{title}</div>
          <div className="detalle-volumen">{volume}</div>
          <div className="detalle-precio mb-3">{price}</div>

          {/* Formulario de Cantidad */}
          <form className="d-flex align-items-center mb-3">
            <label htmlFor="cantidad" className="form-label mb-0">Cantidad:</label>
            <div className="input-group" style={{ width: '120px' }}>
              <button
                type="button"
                className="btn btn-cantidad"
                onClick={() => handleQuantityChange(-1)}
              >
                -
              </button>
              <input
                type="text"
                id="cantidad"
                className="form-control cantidad-input"
                value={quantity}
                readOnly
              />
              <button
                type="button"
                className="btn btn-cantidad"
                onClick={() => handleQuantityChange(1)}
              >
                +
              </button>
            </div>
          </form>

          {/* Botón Agregar a la Bolsa */}
          <button className="btn btn-agregar mb-4" onClick={addToCart}>
            AGREGAR A LA BOLSA
          </button>
          
          {addedToCart && (
            <div className="alert alert-success" role="alert">
              Producto añadido al carrito correctamente.
            </div>
          )}

          {/* Descripción del Producto */}
          <div className="mb-3">
            <h6>¿Qué es?</h6>
            <div className="detalle-descripcion">{description}</div>
          </div>

          {/* Ingredientes */}
          <div className="detalle-ingredientes">
            <h6>Ingredientes</h6>
            {ingredients.map((ingredient, index) => (
              <p key={index}>
                <b>{ingredient.name}:</b> {ingredient.description}
              </p>
            ))}
          </div>
        </div>
      </div>
    </main>
  );
};

ProductDetail.propTypes = {
  id_producto: PropTypes.number.isRequired,
  image: PropTypes.string.isRequired,
  title: PropTypes.string.isRequired,
  volume: PropTypes.string.isRequired,
  price: PropTypes.string.isRequired,
  description: PropTypes.string.isRequired,
  ingredients: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string.isRequired,
      description: PropTypes.string.isRequired,
    })
  ).isRequired,
};

export default ProductDetail;