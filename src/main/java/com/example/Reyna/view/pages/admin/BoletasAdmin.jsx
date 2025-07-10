import React, { useState } from 'react';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import { jsPDF } from 'jspdf';

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
      const token = localStorage.getItem('token');
      const headers = {
        'Authorization': `Bearer ${token}`,
        'Content-Type': 'application/json'
      };

      if (codigo) {
        // Buscar por código de boleta
        const res = await fetch(`/api/boletas/buscar/codigo?codigo=${codigo}`, { headers });
        if (!res.ok) throw new Error('No se encontró la boleta');
        const data = await res.json();
        setResultados([data]);
      } else if (fecha) {
        // Buscar por fecha (rango de un solo día)
        const inicio = `${fecha}T00:00:00`;
        const fin = `${fecha}T23:59:59`;
        const res = await fetch(`/api/boletas/buscar/fecha?inicio=${inicio}&fin=${fin}`, { headers });
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

  const handleDownloadPDF = (boleta) => {
    const doc = new jsPDF({ unit: 'mm', format: 'a4' });
    const fechaVenta = new Date(boleta.fechaVenta);
    const pad = n => n.toString().padStart(2, '0');
    const fecha = `${pad(fechaVenta.getDate())}/${pad(fechaVenta.getMonth() + 1)}/${fechaVenta.getFullYear()}`;
    const hora = `${pad(fechaVenta.getHours())}:${pad(fechaVenta.getMinutes())}`;

    // Generar el PDF con el mismo formato que el cliente
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
    doc.text(`N° de Boleta: ${boleta.codigo}          Fecha: ${fecha}          Hora: ${hora}`, 15, y); y += 7;
    doc.text('Cliente:', 15, y); y += 5;
    doc.text(`Nombre: ${boleta.cliente?.nombre || ''}`, 15, y); y += 5;
    doc.text(`Apellido: ${boleta.cliente?.apellido || ''}`, 15, y); y += 5;
    doc.text(`Teléfono: ${boleta.cliente?.telefono || ''}`, 15, y); y += 5;
    doc.text(`Correo: ${boleta.cliente?.correo || ''}`, 15, y); y += 5;
    doc.text(`Dirección: ${boleta.cliente?.direccion || ''}`, 15, y); y += 7;
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 5;
    doc.text('| Cant  | Producto                       | P. Unit | Total |', 15, y); y += 5;
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 5;
    
    // Agregar productos
    if (boleta.detalles && boleta.detalles.length > 0) {
      boleta.detalles.forEach(detalle => {
        const nombreCorto = detalle.producto?.nombre_producto?.slice(0, 22) || 'Producto';
        doc.text(`|  ${detalle.cantidad}   | ${nombreCorto} | S/.${detalle.precioUnitario} | S/.${detalle.subtotal?.toFixed(2)} |`, 15, y);
        y += 5;
      });
    } else {
      // Si no hay detalles, mostrar el total como un solo producto
      doc.text(`|  1   | Productos varios              | S/.${boleta.total?.toFixed(2)} | S/.${boleta.total?.toFixed(2)} |`, 15, y);
      y += 5;
    }
    
    doc.text('------------------------------------------------------------------------------------------------------------------------', 15, y); y += 6;
    doc.text(`Subtotal:   S/.${boleta.subtotal?.toFixed(2)}`, 120, y); y += 5;
    doc.text(`IGV (18%):   S/.${boleta.igv?.toFixed(2)}`, 120, y); y += 5;
    doc.setFont(undefined, 'bold');
    doc.text(`TOTAL:    S/.${boleta.total?.toFixed(2)}`, 120, y); y += 7;
    doc.setFont(undefined, 'normal');
    doc.text('Método de Pago: Transferencia vía Yape', 15, y); y += 5;
    doc.text(`Código de Pedido: ${boleta.codigo}`, 15, y); y += 7;
    doc.setFontSize(11);
    doc.text('**Gracias por su compra. Será confirmada en breve por el equipo.**', 15, y);

    // Descargar PDF
    doc.save(`boleta-${boleta.codigo}.pdf`);
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
            <input 
              type="text" 
              value={codigo} 
              onChange={e => setCodigo(e.target.value)} 
              placeholder="Ej: BOL-A1B2C3D4" 
              style={{ width: '100%', padding: 6 }} 
            />
          </div>
          <button type="submit" style={{ width: '100%', padding: 10, background: '#97082c', color: 'white', border: 'none', borderRadius: 4 }}>Buscar</button>
        </form>
        {error && <div style={{ color: 'red', marginTop: 16 }}>{error}</div>}
        {resultados && resultados.length > 0 && (
          <div style={{ marginTop: 24 }}>
            {resultados.map(boleta => (
              <div key={boleta.id} style={{ border: '1px solid #eee', borderRadius: 6, padding: 16, marginBottom: 16, fontFamily: 'monospace', fontSize: '12px' }}>
                <div style={{ textAlign: 'center', fontWeight: 'bold', fontSize: '14px', marginBottom: 10 }}>
                  *** PERFUMERÍA LA REYNA ***
                </div>
                <div style={{ fontSize: '10px', marginBottom: 5 }}>RUC: 10097930223</div>
                <div style={{ fontSize: '10px', marginBottom: 5 }}>Dirección: Av. Angelica Gamarra 1320, Los Olivos - Perú</div>
                <div style={{ fontSize: '10px', marginBottom: 10 }}>Tel: +51 986 140 637 | tienda@lareyna.com</div>
                
                <div style={{ borderTop: '1px solid #333', borderBottom: '1px solid #333', textAlign: 'center', fontWeight: 'bold', padding: '5px 0', marginBottom: 10 }}>
                  BOLETA DE VENTA
                </div>
                
                <div style={{ marginBottom: 10 }}>
                  <strong>N° de Boleta:</strong> {boleta.codigo} &nbsp;&nbsp;&nbsp;
                  <strong>Fecha:</strong> {new Date(boleta.fechaVenta).toLocaleDateString('es-PE')} &nbsp;&nbsp;&nbsp;
                  <strong>Hora:</strong> {new Date(boleta.fechaVenta).toLocaleTimeString('es-PE', { hour: '2-digit', minute: '2-digit' })}
                </div>
                
                <div style={{ marginBottom: 10 }}>
                  <div><strong>Cliente:</strong></div>
                  <div>Nombre: {boleta.cliente?.nombre}</div>
                  <div>Apellido: {boleta.cliente?.apellido}</div>
                  <div>Teléfono: {boleta.cliente?.telefono}</div>
                  <div>Correo: {boleta.cliente?.correo}</div>
                  <div>Dirección: {boleta.cliente?.direccion}</div>
                </div>
                
                <table style={{ width: '100%', borderCollapse: 'collapse', fontSize: '10px', marginBottom: 10 }}>
                  <thead>
                    <tr style={{ borderTop: '1px solid #333', borderBottom: '1px solid #333' }}>
                      <th style={{ textAlign: 'left', padding: '2px' }}>Cant</th>
                      <th style={{ textAlign: 'left', padding: '2px' }}>Producto</th>
                      <th style={{ textAlign: 'right', padding: '2px' }}>P. Unit</th>
                      <th style={{ textAlign: 'right', padding: '2px' }}>Total</th>
                    </tr>
                  </thead>
                  <tbody>
                    {boleta.detalles?.map(det => (
                      <tr key={det.id}>
                        <td style={{ padding: '2px' }}>{det.cantidad}</td>
                        <td style={{ padding: '2px' }}>{det.producto?.nombre_producto?.slice(0, 22)}</td>
                        <td style={{ textAlign: 'right', padding: '2px' }}>S/.{det.precioUnitario}</td>
                        <td style={{ textAlign: 'right', padding: '2px' }}>S/.{det.subtotal?.toFixed(2)}</td>
                      </tr>
                    ))}
                  </tbody>
                </table>
                
                <div style={{ borderTop: '1px solid #333', paddingTop: 5 }}>
                  <div style={{ textAlign: 'right', marginBottom: 5 }}>
                    Subtotal: S/.{boleta.subtotal?.toFixed(2)}
                  </div>
                  <div style={{ textAlign: 'right', marginBottom: 5 }}>
                    IGV (18%): S/.{boleta.igv?.toFixed(2)}
                  </div>
                  <div style={{ textAlign: 'right', fontWeight: 'bold', marginBottom: 10 }}>
                    TOTAL: S/.{boleta.total?.toFixed(2)}
                  </div>
                </div>
                
                <div style={{ marginBottom: 5 }}>Método de Pago: Transferencia vía Yape</div>
                <div style={{ marginBottom: 10 }}>Código de Pedido: {boleta.codigo}</div>
                
                <div style={{ textAlign: 'center', fontWeight: 'bold', fontSize: '11px' }}>
                  **Gracias por su compra. Será confirmada en breve por el equipo.**
                </div>
                
                <div style={{ textAlign: 'center', marginTop: 15 }}>
                  <button 
                    onClick={() => handleDownloadPDF(boleta)} 
                    style={{ 
                      padding: '8px 16px', 
                      background: '#97082c', 
                      color: 'white', 
                      border: 'none', 
                      borderRadius: 4, 
                      cursor: 'pointer',
                      fontSize: '12px'
                    }}
                  >
                    Descargar PDF
                  </button>
                </div>
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
