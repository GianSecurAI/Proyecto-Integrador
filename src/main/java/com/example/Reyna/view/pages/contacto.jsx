import React from 'react';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Contacto.css'; 

const ContactoPage = () => {
  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      {/* Hero Section */}
      <section className="hero-section py-5">
        <div className="container py-5">
          <div className="d-flex align-items-center" style={{ marginLeft: '15%' }}>
            <h2 className="mb-0 me-5">CONTÁCTANOS</h2>
            <hr />
          </div>
        </div>
      </section>

      {/* Contact Section */}
      <section className="py-5" style={{ background: '#f8f6f8' }}>
        <div className="container text-center">
          <div className="mb-4 mt-5">
            <h5 className="fw-normal" style={{ letterSpacing: '1px' }}>
              ¿TIENES DUDAS, SUGERENCIAS O TE GUSTARÍA HACER UN PEDIDO ESPECIAL?
            </h5>
            <p className="mb-3">
              En LA REYNA, estamos felices de ayudarte a encontrar lo mejor en maquillaje, perfumes, cremas y joyas.
              <br />
              ¡Tu belleza y confianza son nuestra prioridad!
            </p>
          </div>

          <div className="row justify-content-center mt-5 mb-5">
            <h5 className="text-center mb-4">O ESCRÍBENOS...</h5>
            <div className="col-md-3 mb-4">
              <a href="mailto:demo@lareyna.com" className="contact-card email-card">
                <div className="card h-100 border-0 shadow-sm">
                  <div className="card-body text-center py-4">
                    <i className="bi bi-envelope-fill display-4"></i>
                    <h5 className="card-title mt-3 mb-3">CORREO</h5>
                    <p className="card-text">tienda@lareyna.com</p>
                  </div>
                </div>
              </a>
            </div>
            
            <div className="col-md-3 mb-4">
              <a href="https://wa.me/34947002963" className="contact-card whatsapp-card" target="_blank" rel="noopener noreferrer">
                <div className="card h-100 border-0 shadow-sm">
                  <div className="card-body text-center py-4">
                    <i className="bi bi-whatsapp display-4"></i>
                    <h5 className="card-title mt-3 mb-3">WHATSAPP</h5>
                    <p className="card-text">(+51) 973 908 601</p>
                  </div>
                </div>
              </a>
            </div>
            
            <div className="col-md-3 mb-4">
              <a href="https://instagram.com/lareyna" className="contact-card instagram-card" target="_blank" rel="noopener noreferrer">
                <div className="card h-100 border-0 shadow-sm">
                  <div className="card-body text-center py-4">
                    <i className="bi bi-instagram display-4"></i>
                    <h5 className="card-title mt-3 mb-3">INSTAGRAM</h5>
                    <p className="card-text">@lareyna</p>
                  </div>
                </div>
              </a>
            </div>
            
            <div className="col-md-3 mb-4">
              <a href="https://facebook.com/LaReynaOficial" className="contact-card facebook-card" target="_blank" rel="noopener noreferrer">
                <div className="card h-100 border-0 shadow-sm">
                  <div className="card-body text-center py-4">
                    <i className="bi bi-facebook display-4"></i>
                    <h5 className="card-title mt-3 mb-3">FACEBOOK</h5>
                    <p className="card-text">La Reyna Oficial</p>
                  </div>
                </div>
              </a>
            </div>
          </div>
        </div>
      </section>

      {/* Map Section */}
      <div className="container map-container">
        <div className="row justify-content-center">
          <div className="col-md-0">
            <iframe
              src="https://www.google.com/maps/embed?pb=!1m18!1m12!1m3!1d975.6354363323752!2d-77.07598562433225!3d-12.006178285597429!2m3!1f0!2f0!3f0!3m2!1i1024!2i768!4f13.1!3m3!1m2!1s0x9105cef3cc0c8e2f%3A0xbd9fd599cfab4942!2sLa%20Reyna!5e0!3m2!1ses-419!2spe!4v1752099672042!5m2!1ses-419!2spe"
              width="100%"
              height="400"
              allowFullScreen=""
              loading="lazy"
              referrerPolicy="no-referrer-when-downgrade"
            ></iframe>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default ContactoPage;