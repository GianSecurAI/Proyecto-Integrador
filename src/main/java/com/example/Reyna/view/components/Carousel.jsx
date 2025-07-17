import React, { useEffect, useState } from 'react';
import bannerPerfume1 from '../../../../../../../assets/img/banner-perfume.png';
import bannerPerfume2 from '../../../../../../../assets/img/banner-perfume2.png';
import bannerPerfume3 from '../../../../../../../assets/img/banner-perfume3.png';
import bannerPerfume4 from '../../../../../../../assets/img/banner-perfume4.png';

const Carousel = () => {
  const [currentSlide, setCurrentSlide] = useState(0);
  const slides = [bannerPerfume1, bannerPerfume2, bannerPerfume3, bannerPerfume4];

  useEffect(() => {
    // Crear un intervalo automático independiente de Bootstrap
    const interval = setInterval(() => {
      setCurrentSlide(prev => (prev + 1) % slides.length);
    }, 2500); // 2.5 segundos

    // Limpiar el intervalo cuando el componente se desmonte
    return () => clearInterval(interval);
  }, [slides.length]);

  const goToSlide = (index) => {
    setCurrentSlide(index);
  };

  const goToPrev = () => {
    setCurrentSlide(prev => prev === 0 ? slides.length - 1 : prev - 1);
  };

  const goToNext = () => {
    setCurrentSlide(prev => (prev + 1) % slides.length);
  };

  return (
    <section id="banner-principal" className="carousel slide" style={{ position: 'relative' }}>
      <div className="carousel-inner">
        {slides.map((slide, index) => (
          <div 
            key={index}
            className={`carousel-item ${index === currentSlide ? 'active' : ''}`}
            style={{ 
              display: index === currentSlide ? 'block' : 'none'
            }}
          >
            <img src={slide} className="d-block w-100" alt={`Banner Perfume ${index + 1}`} />
          </div>
        ))}
      </div>
      <button 
        className="carousel-control-prev" 
        type="button" 
        onClick={goToPrev}
        style={{ position: 'absolute', left: 0, top: '50%', transform: 'translateY(-50%)', zIndex: 5 }}
      >
        <span className="carousel-control-prev-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Previous</span>
      </button>
      <button 
        className="carousel-control-next" 
        type="button" 
        onClick={goToNext}
        style={{ position: 'absolute', right: 0, top: '50%', transform: 'translateY(-50%)', zIndex: 5 }}
      >
        <span className="carousel-control-next-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Next</span>
      </button>
    </section>
  );
};

export default Carousel;