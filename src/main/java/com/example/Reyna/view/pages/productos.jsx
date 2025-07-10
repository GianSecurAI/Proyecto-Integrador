import React, { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import ProductCard from '../components/ProductCard';
import ProductMenu from '../components/ProductMenu';
import Pagination from '../components/Pagination';

import prodFrag1Img from '../../../../../../../assets/img/prod-frag1.png';
import prodFrag2Img from '../../../../../../../assets/img/prod-frag2.png';
import prodFrag3Img from '../../../../../../../assets/img/prod-frag3.png';
import prodFrag4Img from '../../../../../../../assets/img/prod-frag4.jpg';
import prodFrag5Img from '../../../../../../../assets/img/prod-frag5.jpg';
import prodFrag6Img from '../../../../../../../assets/img/prod-frag6.png';
import prodFrag7Img from '../../../../../../../assets/img/prod-frag7.png';
import prodFrag8Img from '../../../../../../../assets/img/prod-frag8.png';
import prodFrag9Img from '../../../../../../../assets/img/prod-frag9.png';
import prodFrag10Img from '../../../../../../../assets/img/prod-frag10.png';
import prodFrag11Img from '../../../../../../../assets/img/prod-frag11.png';
import prodFrag12Img from '../../../../../../../assets/img/prod-frag12.png';
import fondoImg from '../../../../../../../assets/img/fondo.png';
import '../styles/Productos.css'; // Importar el CSS para la página de productos

const ProductsPage = () => {
  const [allProducts, setAllProducts] = useState([]);
  const [filteredProducts, setFilteredProducts] = useState([]);
  const [selectedCategory, setSelectedCategory] = useState(null);
  const [currentPage, setCurrentPage] = useState(1);

  // Mapeo de imágenes (temporal hasta que las imágenes vengan de la API)
  const productImages = {
    'P001': prodFrag1Img,
    'P002': prodFrag2Img,
    'P003': prodFrag3Img,
    'P004': prodFrag4Img,
    'P005': prodFrag5Img,
    'P006': prodFrag6Img,
    'P007': prodFrag7Img,
    'P008': prodFrag8Img,
    'P009': prodFrag9Img,
    'P010': prodFrag10Img,
    'P011': prodFrag11Img,
    'P012': prodFrag12Img,
    // Agrega más mapeos si es necesario, usando el código de producto
  };

  useEffect(() => {
    // Cargar todos los productos desde la API
    fetch('/api/productos')
      .then(response => {
        if (!response.ok) {
          throw new Error(`Error del servidor: ${response.status} ${response.statusText}`);
        }
        return response.json();
      })
      .then(data => {
        setAllProducts(data);
        setFilteredProducts(data); // Inicialmente mostrar todos
      })
      .catch(error => {
        console.error('Error al cargar productos:', error);
      });
  }, []);

  useEffect(() => {
    // Filtrar productos cuando cambia la categoría seleccionada
    if (selectedCategory === null) {
      setFilteredProducts(allProducts);
    } else {
      const filtered = allProducts.filter(
        product => product.categoria.id_categoria === selectedCategory
      );
      setFilteredProducts(filtered);
    }
    setCurrentPage(1); // Resetear a la primera página con cada filtro
  }, [selectedCategory, allProducts]);

  // Productos por página
  const productsPerPage = 6;
  
  // Calcular total de páginas
  const totalPages = Math.ceil(filteredProducts.length / productsPerPage);
  
  // Obtener los productos de la página actual
  const getCurrentPageProducts = () => {
    const startIndex = (currentPage - 1) * productsPerPage;
    const endIndex = startIndex + productsPerPage;
    return filteredProducts.slice(startIndex, endIndex);
  };
  
  // Manejar cambio de página
  const handlePageChange = (page) => {
    setCurrentPage(page);
    // Desplazar la vista hacia arriba cuando se cambia de página
    window.scrollTo(0, 0);
  };
  
  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      {/* Header de Productos */}
      <section className="productos-header text-center py-5" 
              style={{
                  backgroundImage: `url(${fondoImg})`,
                }}>        
                <div className="container py-5">
          <div className="d-flex align-items-center" style={{ marginLeft: '15%' }}>
            <h1 className="mb-0 me-5">NUESTROS PRODUCTOS</h1>
            <hr />
          </div>
        </div>
      </section>

      {/* Contenido Principal */}
      <main className="container py-5">
        <div className="row">
          {/* Menú Lateral */}
          <aside className="col-lg-3">
            <ProductMenu onCategorySelect={setSelectedCategory} />
          </aside>

          {/* Productos */}
          <section className="col-lg-9">
            <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
              {getCurrentPageProducts().map((product) => (
                <div key={product.id_producto} className="col">
                  <ProductCard
                    image={productImages[product.codigo] || prodFrag5Img} // Usa una imagen por defecto si no hay mapeo
                    name={product.nombre_producto}
                    price={`S/ ${product.precio.toFixed(2)}`}
                    link={`/producto/${product.id_producto}`} // Enlace dinámico con el ID
                  />
                </div>
              ))}
            </div>

            {/* Paginación */}
            <Pagination
              currentPage={currentPage}
              totalPages={totalPages}
              onPageChange={handlePageChange}
            />
          </section>
        </div>
      </main>

      <Footer />
    </div>
  );
};

export default ProductsPage;