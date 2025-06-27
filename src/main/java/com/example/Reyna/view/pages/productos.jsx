import React from 'react';
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
  const [currentPage, setCurrentPage] = React.useState(1);
  
  // Lista de todos los productos disponibles
  const allProducts = [
    // Página 1
    {
      id: 1,
      image: prodFrag1Img,
      name: "Set Ccori Rosé: Parfum + Loción Perfumada",
      price: "S/ 119.00",
      link: "/producto/ccori-rose"
    },
    {
      id: 2,
      image: prodFrag2Img,
      name: "Cielo en Rosa Eau de Parfum",
      price: "S/ 113.00",
      link: "/producto/cielo-rosa"
    },
    {
      id: 3,
      image: prodFrag3Img,
      name: "Set Sauvage Dior: Parfum + Estuche Elegante",
      price: "S/ 105.00",
      link: "/producto/sauvage-dior"
    },
    {
      id: 4,
      image: prodFrag6Img,
      name: "Bombshell Seduction Eau de Parfum",
      price: "S/ 180.00",
      link: "/producto/prod-frag6"
    },
    {
      id: 5,
      image: prodFrag5Img,
      name: "Una Instinct Eau de Parfum",
      price: "S/ 165.00",
      link: "/producto/prod-frag5"
    },
    {
      id: 6,
      image: prodFrag4Img,
      name: "Fragancia Intensa para Hombre",
      price: "S/ 150.00",
      link: "/producto/prod-frag4"
    },
    // Página 2
    {
      id: 7,
      image: prodFrag7Img,
      name: "Euforia Floral Collection",
      price: "S/ 145.00",
      link: "/producto/euforia-floral"
    },
    {
      id: 8,
      image: prodFrag8Img,
      name: "Set Elegance: Perfume + Body Lotion",
      price: "S/ 135.00",
      link: "/producto/elegance-set"
    },
    {
      id: 9,
      image: prodFrag9Img,
      name: "Midnight Dreams Eau de Parfum",
      price: "S/ 190.00",
      link: "/producto/midnight-dreams"
    },
    {
      id: 10,
      image: prodFrag10Img,
      name: "Sweet Garden Collection",
      price: "S/ 170.00",
      link: "/producto/sweet-garden"
    },
    {
      id: 11,
      image: prodFrag11Img,
      name: "Ocean Breeze For Men",
      price: "S/ 155.00",
      link: "/producto/ocean-breeze"
    },
    {
      id: 12,
      image: prodFrag12Img,
      name: "Diamond Rose Limited Edition",
      price: "S/ 210.00",
      link: "/producto/diamond-rose"
    },
  ];
  
  // Productos por página
  const productsPerPage = 6;
  
  // Calcular total de páginas
  const totalPages = Math.ceil(allProducts.length / productsPerPage);
  
  // Obtener los productos de la página actual
  const getCurrentPageProducts = () => {
    const startIndex = (currentPage - 1) * productsPerPage;
    const endIndex = startIndex + productsPerPage;
    return allProducts.slice(startIndex, endIndex);
  };
  
  // Manejar cambio de página
  const handlePageChange = (page) => {
    setCurrentPage(page);
    // Desplazar la vista hacia arriba cuando se cambia de página
    window.scrollTo(0, 0);
  };
  
  return (
    <div className="page-container-for-fixed-nav"> {/* Aplicada clase para el Navbar fijo */}
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
            <ProductMenu />
          </aside>

          {/* Productos */}
          <section className="col-lg-9">
            <div className="row row-cols-1 row-cols-md-2 row-cols-lg-3 g-4">
              {getCurrentPageProducts().map((product) => (
                <div key={product.id} className="col">
                  <ProductCard
                    image={product.image}
                    name={product.name}
                    price={product.price}
                    link={product.link}
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