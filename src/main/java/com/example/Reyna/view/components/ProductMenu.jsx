import React, { useState, useEffect } from 'react';
import '../styles/ProductMenu.css';

const ProductMenu = ({ onCategorySelect }) => {
  const [categories, setCategories] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null); // Nuevo estado para errores


  useEffect(() => {
    setLoading(true);
    // Hacemos la llamada a la API para obtener las categorías
    fetch('/api/categorias')
      .then(response => {
        if (!response.ok) {
          // Si la respuesta no es exitosa, lanzamos un error para que lo capture el .catch()
           throw new Error(`Error del servidor: ${response.status} ${response.statusText}`);
        }
        return response.json();
      })
      .then(data => {
        setCategories(data);
        
      })
      .catch(error => {
        console.error('Error al cargar las categorías:', error);
        setError(error.message); // Guardamos el mensaje de error para mostrarlo al usuario
      })
      .finally(() => {
        setLoading(false);
      });
  }, []); // El array vacío asegura que se ejecute solo una vez

  return (
    <div className="menu-perfumes">
      <h5>Categorías</h5>
      <nav className="nav flex-column">
        <a
          className="nav-link category-title"
          href="#"
          onClick={(e) => {
            e.preventDefault();
            onCategorySelect(null);}}
        >
          ▸ Todas
          </a>
        {loading && <span className="nav-link">Cargando...</span>}

        {error && <span className="nav-link text-danger">Error al cargar</span>}

        {!loading && !error && categories.map((category) => (
          <button
            key={category.id_categoria}
            className="nav-link category-title"
            onClick={() => onCategorySelect(category.id_categoria)}
          >
            ▸ {category.nombre_categoria}
          </button>
        ))}
      </nav>
    </div>
  );
};

export default ProductMenu;