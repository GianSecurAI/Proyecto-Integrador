import React from 'react';
import '../styles/ProductMenu.css';

const ProductMenu = () => {
  const categories = [
    { title: 'Niños' },
    { title: 'Adolescentes' },
    { title: 'Caballeros' },
    { title: 'Damas' }
  ];

  return (    <div className="menu-perfumes">
      <h5>Categorías</h5>
      <nav className="nav flex-column">
        {categories.map((category, index) => (
          <a
            key={category.title}
            className="nav-link category-title"
            href="#"
            onClick={(e) => {
              e.preventDefault();
              // Aquí puedes agregar la lógica para filtrar los productos
              console.log(`Filtrar por ${category.title}`);
            }}
            style={{
              padding: '10px 15px',
              textDecoration: 'none',
              display: 'block',
              
            }}
          >
            ▸ {category.title}
          </a>
        ))}
      </nav>
    </div>
  );
};

export default ProductMenu;