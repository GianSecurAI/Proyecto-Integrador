import React from 'react';
import bannerPerfume1 from '../../../../../../../assets/img/banner-perfume.png';
import bannerPerfume2 from '../../../../../../../assets/img/banner-perfume2.png';
import bannerPerfume3 from '../../../../../../../assets/img/banner-perfume3.png';
import bannerPerfume4 from '../../../../../../../assets/img/banner-perfume4.png';

const Carousel = () => {
  return (
    <section id="banner-principal" className="carousel slide" data-bs-ride="carousel" data-bs-interval="2500">
      <div className="carousel-inner">
        <div className="carousel-item active">
          <img src={bannerPerfume1} className="d-block w-100" alt="Banner Perfume 1" />
        </div>
        <div className="carousel-item">
          <img src={bannerPerfume2} className="d-block w-100" alt="Banner Perfume 2" />
        </div>
        <div className="carousel-item">
          <img src={bannerPerfume3} className="d-block w-100" alt="Banner Perfume 3" />
        </div>
        <div className="carousel-item">
          <img src={bannerPerfume4} className="d-block w-100" alt="Banner Perfume 4" />
        </div>
      </div>
      <button className="carousel-control-prev" type="button" data-bs-target="#banner-principal" data-bs-slide="prev">
        <span className="carousel-control-prev-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Previous</span>
      </button>
      <button className="carousel-control-next" type="button" data-bs-target="#banner-principal" data-bs-slide="next">
        <span className="carousel-control-next-icon" aria-hidden="true"></span>
        <span className="visually-hidden">Next</span>
      </button>
    </section>
  );
};

export default Carousel;