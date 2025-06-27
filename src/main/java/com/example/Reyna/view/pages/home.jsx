import React from 'react';
import Navbar from '../components/Navbar';
import Carousel from '../components/Carousel';
import CategoryCard from '../components/CategoryCard';
import ProductCard from '../components/ProductCard';
import PromoCard from '../components/PromoCard';
import Footer from '../components/Footer';
import '../styles/Home.css'; // Importar Home.css

// Importar imágenes desde src/assets/img
import categoriaFraganciasImg from '../../../../../../../assets/img/categoria-fragancias.png';
import categoriaTratamientosImg from '../../../../../../../assets/img/categoria-tratamientos.png';
import categoriaMaquillajeImg from '../../../../../../../assets/img/categoria-maquillaje.png';
import categoriaCuidadoImg from '../../../../../../../assets/img/categoria-cuidado.png';
import prodFrag1Img from '../../../../../../../assets/img/prod-frag1.png';
import prodFrag2Img from '../../../../../../../assets/img/prod-frag2.png';
import prodFrag3Img from '../../../../../../../assets/img/prod-frag3.png';
import prodFrag6Img from '../../../../../../../assets/img/prod-frag6.png';
import promoRegaloBgImg from '../../../../../../../assets/img/promo-regalo.png';
import promoDescuentoBgImg from '../../../../../../../assets/img/promo-descuento.png';

const Home = () => {
  return (
    <div className="home-container"> {/* Añadir clase aquí */}
      <Navbar/>
      <Carousel/>
      {/* Título de Categorías */}
      <div className="section-title-container">
        <h2 className="section-title">CATEGORÍAS</h2>
      </div>
      <section className="categorias py-3 py-md-5">
        <div className="container">
          <div className="row g-3 g-md-4 justify-content-center">
            <div className="col-6 col-md-3 mb-3">
              <CategoryCard image={categoriaFraganciasImg} title="FRAGANCIAS" />
            </div>
            <div className="col-6 col-md-3 mb-3">
              <CategoryCard image={categoriaTratamientosImg} title="TRATAMIENTOS FACIALES" />
            </div>
            <div className="col-6 col-md-3 mb-3">
              <CategoryCard image={categoriaMaquillajeImg} title="MAQUILLAJE" />
            </div>
            <div className="col-6 col-md-3 mb-3">
              <CategoryCard image={categoriaCuidadoImg} title="CUIDADO PERSONAL" />
            </div>
          </div>
        </div>
      </section>
      {/* Título de Productos Estrella */}
      <div className="section-title-container productos-estrella-title-container">
        <h2 className="section-title">NUESTROS PRODUCTOS ESTRELLA</h2>
      </div>      <section className="productos-estrella py-4 py-md-5" style={{ background: '#f8f3f7' }}>
        <div className="container">
          <div className="row g-3 g-md-4 justify-content-center">
            <div className="col-6 col-sm-6 col-md-4 col-lg-3 mb-4">
              <ProductCard 
                image={prodFrag1Img} 
                name="Set Ccori Rosé: Parfum + Loción Perfumada" 
                price="S/ 119.00" 
                link="/producto/ccori-rose" 
              />
            </div>
            <div className="col-6 col-sm-6 col-md-4 col-lg-3 mb-4">
              <ProductCard 
                image={prodFrag2Img} 
                name="Cielo en Rosa Eau de Parfum" 
                price="S/ 113.00" 
                link="/producto/cielo-rosa" 
              />
            </div>
            <div className="col-6 col-sm-6 col-md-4 col-lg-3 mb-4">
              <ProductCard 
                image={prodFrag3Img} 
                name="Set Sauvage Dior: Parfum + Estuche Elegante" 
                price="S/ 105.00" 
                link="/producto/sauvage-dior" 
              />
            </div>
            <div className="col-6 col-sm-6 col-md-4 col-lg-3 mb-4">
              <ProductCard 
                image={prodFrag6Img} 
                name="Bombshell Seduction Eau de Parfum" 
                price="S/ 180.00" 
                link="/producto/prod-frag6" 
              />
            </div>
          </div>
        </div>
      </section>
      <section className="container py-4 py-md-5">
        <div className="row g-4">
          <div className="col-12 col-md-6 mb-3 mb-md-0">
            <PromoCard
              backgroundImage={promoRegaloBgImg}
              title="¡REGALO POR COMPRA!"
              description="Llévate un producto de regalo por compras mayores a S/150"
            />
          </div>
          <div className="col-12 col-md-6">
            <PromoCard
              backgroundImage={promoDescuentoBgImg}
              title="HASTA UN 50% EN PRODUCTOS SELECCIONADOS"
              description=""
            />
          </div>
        </div>
      </section>
      <Footer />
    </div>
  );
};

export default Home;