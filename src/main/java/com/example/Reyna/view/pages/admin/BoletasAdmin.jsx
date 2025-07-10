import React, { useState } from 'react';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';

const BoletasAdmin = () => {
  const [fecha, setFecha] = useState('');
  const [codigo, setCodigo] = useState('');
  const [resultados, setResultados] = useState(null);
  const [error, setError] = useState('');

  const handleBuscar = async (e) => {
    e.preventDefault();
    setError('');
    setResultados(null);
    try {
      if (codigo) {
        // Buscar por código (id de boleta)
        const res = await fetch(`/api/boletas/buscar/codigo?id=${codigo}`);
        if (!res.ok) throw new Error('No se encontró la boleta');
        const data = await res.json();
        setResultados([data]);
      } else if (fecha) {
        // Buscar por fecha (rango de un solo día)
        const inicio = `${fecha}T00:00:00`;
        const fin = `${fecha}T23:59:59`;
        const res = await fetch(`/api/boletas/buscar/fecha?inicio=${inicio}&fin=${fin}`);
        if (!res.ok) throw new Error('No se encontraron boletas para esa fecha');
        const data = await res.json();
        setResultados(data);
      } else {
        setError('Ingrese una fecha o un código de venta');
      }
    } catch (err) {
      setError(err.message);
    }
  };

  const handleDownloadPDF = (id) => {
    window.open(`/api/boletas/pdf/${id}`, '_blank');
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      <div style={{ maxWidth: 400, margin: '40px auto', padding: 24, border: '1px solid #ccc', borderRadius: 8, background: '#fff' }}>
        <h3 style={{ marginBottom: 20 }}>Buscar Boleta</h3>
        <form onSubmit={handleBuscar}>
          <div style={{ marginBottom: 16 }}>
            <label>Fecha de Venta</label><br />
            <input type="date" value={fecha} onChange={e => setFecha(e.target.value)} style={{ width: '100%', padding: 6 }} />
          </div>
          <div style={{ marginBottom: 16 }}>
            <label>Código de Venta</label><br />
            <input type="number" value={codigo} onChange={e => setCodigo(e.target.value)} placeholder="ID de boleta" style={{ width: '100%', padding: 6 }} />
          </div>
          <button type="submit" style={{ width: '100%', padding: 10, background: '#8bb6e0', border: 'none', borderRadius: 4 }}>Buscar</button>
        </form>
        {error && <div style={{ color: 'red', marginTop: 16 }}>{error}</div>}
        {resultados && resultados.length > 0 && (
          <div style={{ marginTop: 24 }}>
            {resultados.map(boleta => (
              <div key={boleta.id} style={{ border: '1px solid #eee', borderRadius: 6, padding: 16, marginBottom: 16 }}>
                <div style={{ fontWeight: 'bold', fontSize: 18, textAlign: 'center', marginBottom: 8 }}>Boleta de Ventas<br />La Reyna</div>
                <div style={{ fontWeight: 'bold', textAlign: 'center', marginBottom: 8 }}>BOLETA DE VENTA ELECTRÓNICA</div>
                <div style={{ marginBottom: 6 }}><b>Fecha de Creación:</b> {boleta.fechaVenta?.split('T')[0]}</div>
                <div style={{ marginBottom: 6 }}><b>Cliente:</b> {boleta.cliente?.nombre} {boleta.cliente?.apellido}</div>
                <div style={{ marginBottom: 6 }}><b>Tipo de Comprobante:</b> {boleta.tipoComprobante}</div>
                <div style={{ marginBottom: 6 }}><b>Código de Venta:</b> {boleta.id}</div>
                <hr />
                <table style={{ width: '100%', fontSize: 13, marginBottom: 8 }}>
                  <thead>
                    <tr style={{ borderBottom: '1px solid #ccc' }}>
                      <th style={{ textAlign: 'left' }}>CANT</th>
                      <th style={{ textAlign: 'left' }}>DESCRIPCIÓN</th>
                      <th style={{ textAlign: 'right' }}>PRECIO</th>
                      <th style={{ textAlign: 'right' }}>IMPORTE</th>
                    </tr>
                  </thead>
                  <tbody>
                    {boleta.detalles?.map(det => (
                      <tr key={det.id}>
                        <td>{det.cantidad}</td>
                        <td>{det.producto?.nombre_producto}</td>
                        <td style={{ textAlign: 'right' }}>S/.{det.precioUnitario}</td>
                        <td style={{ textAlign: 'right' }}>S/.{det.subtotal}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                <div style={{ textAlign: 'right', fontSize: 13 }}>
                  <div>D.P GRAVADA: S/.{(boleta.total / 1.18).toFixed(2)}</div>
                  <div>IGV (18%): S/.{(boleta.total - boleta.total / 1.18).toFixed(2)}</div>
                  <div style={{ fontWeight: 'bold' }}>TOTAL A PAGAR: S/.{boleta.total?.toFixed(2)}</div>
                </div>
                <div style={{ textAlign: 'center', marginTop: 12, fontWeight: 'bold' }}>
                  ¡GRACIAS POR SU COMPRA!
                </div>
                {boleta.pdf && (
                  <div style={{ textAlign: 'center', marginTop: 10 }}>
                    <button onClick={() => handleDownloadPDF(boleta.id)} style={{ padding: '5px 10px', background: '#4CAF50', color: 'white', border: 'none', borderRadius: 4, cursor: 'pointer' }}>
                      Descargar PDF
                    </button>
                  </div>
                )}
              </div>
            ))}
          </div>
        )}
        {resultados && resultados.length === 0 && <div style={{ marginTop: 24 }}>No se encontraron boletas.</div>}
      </div>

      <Footer />
    </div>
  );
};

export default BoletasAdmin;
