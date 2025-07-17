import React, { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Checkout.css';
import qrImg from '../../../../../../../assets/img/qr.jpg';
import fondoImg from '../../../../../../../assets/img/fondo.png';

const PagoYapePage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  // Recibe datos del pedido y usuario desde el checkout
  const { total = 0, userData = {}, cartItems = [], deliveryMethod = 'tienda', deliveryFee = 0 } = location.state || {};

  const [comprobante, setComprobante] = useState(null);
  const [tipoComprobante, setTipoComprobante] = useState('boleta');
  const [ruc, setRuc] = useState('');
  const [error, setError] = useState('');

  const handleFileChange = (e) => {
    setComprobante(e.target.files[0]);
  };

  const handleTipoChange = (e) => {
    setTipoComprobante(e.target.value);
    if (e.target.value === 'boleta') setRuc('');
  };

  const handleNext = (e) => {
    e.preventDefault();
    if (tipoComprobante === 'factura' && (!ruc || ruc.length !== 11)) {
      setError('El RUC debe tener 11 dígitos');
      return;
    }
    if (!comprobante) {
      setError('Adjunta el comprobante de pago');
      return;
    }
    setError('');
    // Aquí podrías guardar el comprobante en backend si lo deseas
    navigate('/confirmacion', { state: { total, tipoComprobante, ruc, cliente: userData, productos: cartItems, deliveryMethod, deliveryFee } });
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <section className="checkout-header text-center" style={{ backgroundImage: `url(${fondoImg})`, backgroundSize: 'cover', backgroundPosition: 'center' }}>
        <div className="container">
          <h1>PROCESO DE PAGO</h1>
        </div>
      </section>

      <main className="container py-5 checkout-container">
        <div className="row justify-content-center">
          <div className="col-lg-7">
            <div className="yape-box p-4 shadow rounded bg-white">
              <h3 className="mb-4" style={{ fontWeight: 'bold', color: '#97082c', fontStyle: 'italic', textAlign: 'center', textDecoration: 'underline' }}>Pago con Yape</h3>
              <div className="d-flex flex-wrap align-items-center mb-4">
                <img src={qrImg} alt="QR Yape" style={{ width: 250, height: 250, borderRadius: 12, border: '1px solid #eee', marginRight: 32 }} />
                <div>
                  <div className="mb-2"><strong>Nombre Empresa:</strong> La Reyna</div>
                  <div className="mb-2"><strong>Número móvil:</strong> +51 986 140 637</div>
                  <div className="mb-2"><strong>Monto a pagar:</strong> <span style={{ color: '#be3838', fontWeight: 600 }}>S/ {total.toFixed(2)}</span></div>
                  <div className="mb-2 text-danger"><strong>Tienes 24 horas para cancelar</strong></div>
                </div>
              </div>
              <form onSubmit={handleNext}>
                <div className="mb-3">
                  <label className="form-label">Tipo de comprobante:</label>
                  <div>
                    <label className="me-3">
                      <input type="radio" value="boleta" checked={tipoComprobante === 'boleta'} onChange={handleTipoChange} /> Boleta de Venta Electrónica
                    </label>
                    <label>
                      <input type="radio" value="factura" checked={tipoComprobante === 'factura'} onChange={handleTipoChange} /> Factura Electrónica
                    </label>
                  </div>
                </div>
                {tipoComprobante === 'factura' && (
                  <div className="mb-3">
                    <label className="form-label">RUC:</label>
                    <input type="text" className="form-control" value={ruc} onChange={e => setRuc(e.target.value.replace(/\D/g, '').slice(0, 11))} placeholder="Ingresa RUC (11 dígitos)" required />
                  </div>
                )}
                <div className="mb-3">
                  <label className="form-label">Adjunta el screenshot del pago:</label>
                  <input type="file" className="form-control" accept="image/*" onChange={handleFileChange} required />
                </div>
                {error && <div className="alert alert-danger py-2">{error}</div>}
                <button type="submit" className="btn btn-primary w-100 mt-3">Siguiente</button>
              </form>
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default PagoYapePage;

