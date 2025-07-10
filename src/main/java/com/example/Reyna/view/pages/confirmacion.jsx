import React from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import '../styles/Checkout.css';
import fondoImg from '../../../../../../../assets/img/fondo.png';
import { jsPDF } from 'jspdf';

const ConfirmacionPagoPage = () => {
  const navigate = useNavigate();
  const location = useLocation();
  const { total = 0, tipoComprobante = 'boleta', ruc = '' } = location.state || {};

  const handleDownloadPDF = () => {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    const now = new Date();
    const pad = n => n.toString().padStart(2, '0');
    const fecha = `${pad(now.getDate())}/${pad(now.getMonth() + 1)}/${now.getFullYear()}`;
    const hora = `${pad(now.getHours())}:${pad(now.getMinutes())}`;
    // Número de boleta y código de pedido únicos (simples)
    const boletaNum = Math.floor(100000 + Math.random() * 900000);
    const pedidoCod = `PED-${now.getFullYear()}${pad(now.getMonth() + 1)}${pad(now.getDate())}-${boletaNum}`;

    // Datos del cliente (puedes adaptar los nombres de variables según tu estructura real)
    const cliente = location.state?.cliente || {};
    // Productos del pedido
    const productos = location.state?.productos || [];
    // Si no hay productos, no genera PDF
    if (!productos.length) {
      alert('No se encontraron productos para la boleta.');
      return;
    }
    // Calcular totales
    const subtotal = productos.reduce((sum, p) => sum + (p.price * p.quantity), 0) / 1.18;
    const igv = subtotal * 0.18;
    const total = subtotal + igv;

    let y = 15;
    doc.setFontSize(14);
    doc.text('***    PERFUMERÍA LA REYNA    ***', 15, y); y += 7;
    doc.setFontSize(10);
    doc.text('RUC: 10097930223', 15, y); y += 5;
    doc.text('Dirección: Av. Angelica Gamarra 1320, Los Olivos - Perú', 15, y); y += 5;
    doc.text('Tel: +51 986 140 637                      |                      tienda@lareyna.com', 15, y); y += 7;
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 5;
    doc.setFontSize(12);
    doc.text('BOLETA DE VENTA', 70, y); y += 5;
    doc.setFontSize(10);
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 7;
    doc.text(`N° de Boleta: ${boletaNum}          Fecha: ${fecha}          Hora: ${hora}`, 15, y); y += 7;
    doc.text('Cliente:', 15, y); y += 5;
    doc.text(`Nombre: ${cliente.nombre || ''}`, 15, y); y += 5;
    doc.text(`Apellido: ${cliente.apellido || ''}`, 15, y); y += 5;
    doc.text(`Teléfono: ${cliente.telefono || ''}`, 15, y); y += 5;
    doc.text(`Correo: ${cliente.correo || ''}`, 15, y); y += 5;
    doc.text(`Dirección: ${cliente.direccion || ''}`, 15, y); y += 7;
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 5;
    doc.text('| Cant | Código  | Producto                       | P. Unit | Total |', 15, y); y += 5;
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 5;
    // Productos
    productos.forEach(p => {
      doc.text(`|  ${p.quantity}   | ${p.codigo || p.id || ''}  | ${p.name.slice(0,22)} | S/.${p.price} | S/.${(p.price * p.quantity).toFixed(2)} |`, 15, y);
      y += 5;
    });
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 6;
    doc.text(`Subtotal:   S/.${subtotal.toFixed(2)}`, 120, y); y += 5;
    doc.text(`IGV (18%):   S/.${igv.toFixed(2)}`, 120, y); y += 5;
    doc.setFont(undefined, 'bold');
    doc.text(`TOTAL:    S/.${total.toFixed(2)}`, 120, y); y += 7;
    doc.setFont(undefined, 'normal');
    doc.text('Método de Pago: Transferencia vía Yape', 15, y); y += 5;
    doc.text(`Código de Pedido: ${pedidoCod}`, 15, y); y += 7;
    doc.setFontSize(11);
    doc.text('**Gracias por su compra. Será confirmada en breve por el equipo.**', 15, y);
    doc.save(`boleta-${boletaNum}.pdf`);
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      <section className="checkout-header text-center" style={{ backgroundImage: `url(${fondoImg})`, backgroundSize: 'cover', backgroundPosition: 'center' }}>
        <div className="container">
          <h1>CONFIRMACIÓN DE PAGO</h1>
        </div>
      </section>
      
      <main className="container py-5 checkout-container">
        <div className="row justify-content-center">
          <div className="col-lg-7">
            <div className="confirmacion-box p-4 shadow rounded bg-white text-center">
              <div className="mb-4" style={{ fontSize: 60, color: '#be3838' }}>⏳</div>
              <h3 className="mb-3">¡Tu pago está en proceso de confirmación!</h3>
              <p className="mb-4">Estamos validando tu comprobante. Te notificaremos por correo o WhatsApp cuando tu pago sea aprobado.</p>
              <div className="mb-3"><strong>Monto pagado:</strong> S/ {total.toFixed(2)}</div>
              <div className="mb-3"><strong>Tipo de comprobante:</strong> {tipoComprobante === 'factura' ? 'Factura' : 'Boleta'}</div>
              {tipoComprobante === 'factura' && (
                <div className="mb-3"><strong>RUC:</strong> {ruc}</div>
              )}
              
              {tipoComprobante && (
                <div className="mb-3">
                  <button
                    type="button"
                    className="btn btn-outline-secondary"
                    style={{ marginBottom: 16 }}
                    onClick={handleDownloadPDF}
                  >
                    Descargar {tipoComprobante === 'factura' ? 'Factura' : 'Boleta'} (PDF)
                  </button>
                </div>
              )}
              
              <div className="alert alert-info mt-4">Puedes volver al inicio mientras validamos tu pago.</div>
              <button className="btn btn-primary mt-3" style={{ backgroundColor: '#97082c', borderColor: '#97082c' }} onClick={() => navigate('/')}>Volver al inicio</button>
            </div>
          </div>
        </div>
      </main>
      <Footer />
    </div>
  );
};

export default ConfirmacionPagoPage;
