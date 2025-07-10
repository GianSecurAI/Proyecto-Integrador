import React from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import '../../styles/Dashboard.css';

const Dashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <div className="dashboard-container">
        <h1 className="dashboard-title">Panel de Administración</h1>
        
        <div className="dashboard-sections">
          {/* Sección de Mantenimiento */}
          <div className="dashboard-section">
            <h2>Mantenimiento</h2>
            <div className="dashboard-cards">
              <div 
                className="dashboard-card maintenance"
                onClick={() => navigate('/admin/productos')}
              >
                <h3>Productos</h3>
              </div>
              <div 
                className="dashboard-card maintenance"
                onClick={() => navigate('/admin/clientes')}
              >
                <h3>Clientes</h3>
              </div>
            </div>
          </div>

          {/* Sección de Ventas */}
          <div className="dashboard-section">
            <h2>Ventas</h2>
            <div className="dashboard-cards">
              <div 
                className="dashboard-card sales"
                onClick={() => navigate('/admin/boletas/buscar')}
              >
                <h3>Boletas</h3>
              </div>
            </div>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default Dashboard;
