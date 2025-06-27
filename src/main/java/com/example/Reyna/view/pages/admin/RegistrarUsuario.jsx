import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import apiClient from '../../../api/apiClient';
import '../../styles/RegistrarUsuario.css'; // Estilos para esta página

const RegistrarUsuario = () => {
  const navigate = useNavigate();
  const [nuevoUsuario, setNuevoUsuario] = useState({
    nombre: '', // Cambiado de nombres a nombre
    apellido: '', // Cambiado de apellidos a apellido
    correo: '',
    contraseña: '',
    telefono: '',
    direccion: '',
    id_rol: '' // ID del rol a asignar
  });
  const [errores, setErrores] = useState({});
  const [loading, setLoading] = useState(false);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNuevoUsuario(prev => ({ ...prev, [name]: value }));
  };

  const validarFormulario = () => {
    const nuevosErrores = {};
    if (!nuevoUsuario.nombre.trim()) nuevosErrores.nombre = 'El nombre es requerido'; // Validar nombre
    if (!nuevoUsuario.apellido.trim()) nuevosErrores.apellido = 'El apellido es requerido'; // Validar apellido
    if (!nuevoUsuario.correo.trim()) {
      nuevosErrores.correo = 'El correo es requerido';
    } else if (!/\S+@\S+\.\S+/.test(nuevoUsuario.correo)) {
      nuevosErrores.correo = 'El formato de correo no es válido';
    }
    if (!nuevoUsuario.contraseña) nuevosErrores.contraseña = 'La contraseña es requerida';
    if (!nuevoUsuario.id_rol) nuevosErrores.id_rol = 'Debe seleccionar un rol';

    setErrores(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  const handleGuardarUsuario = async (e) => {
    e.preventDefault();
    if (!validarFormulario()) {
      return;
    }

    setLoading(true);

    const userToCreate = {
      nombre: nuevoUsuario.nombre.trim(), // Enviar nombre por separado
      apellido: nuevoUsuario.apellido.trim(), // Enviar apellido por separado
      correo: nuevoUsuario.correo,
      contraseña: nuevoUsuario.contraseña,
      telefono: nuevoUsuario.telefono,
      direccion: nuevoUsuario.direccion,
      estado: true,
      rol: {
        id_rol: parseInt(nuevoUsuario.id_rol, 10)
      }
    };

    try {
      const response = await apiClient('/admin/register-usuario', {
        method: 'POST',
        body: JSON.stringify(userToCreate),
      });

      if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message || `Error del servidor: ${response.status}`);
      }

      alert('Usuario registrado con éxito!');
      navigate('/admin/dashboard'); // Redirigir al dashboard o a la lista de usuarios

    } catch (error) {
      console.error("Error al registrar usuario:", error);
      setErrores(prev => ({ ...prev, form: error.message }));
      alert(`Error al registrar el usuario: ${error.message}`);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      <div className="registrar-usuario-container">
        <div className="registrar-usuario-card">
          <h1>Registrar Nuevo Usuario</h1>
          <p>Complete el formulario para crear un nuevo vendedor o administrador.</p>
          <hr className="divisor" />

          <form onSubmit={handleGuardarUsuario} noValidate>
            <div className="form-grid-register">
              {/* Columna Izquierda */}
              <div className="form-column-register">
                <div className="form-group">
                  <label htmlFor="nombre">Nombres</label> {/* Cambiado htmlFor */}
                  <input
                    id="nombre" // Cambiado id
                    type="text"
                    name="nombre" // Cambiado name
                    placeholder="Ingrese los nombres"
                    value={nuevoUsuario.nombre}
                    onChange={handleInputChange}
                    className={errores.nombre ? 'input-error' : ''}
                  />
                  {errores.nombre && <span className="error-message">{errores.nombre}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="correo">Correo Electrónico</label>
                  <input
                    id="correo"
                    type="email"
                    name="correo"
                    placeholder="ejemplo@correo.com"
                    value={nuevoUsuario.correo}
                    onChange={handleInputChange}
                    className={errores.correo ? 'input-error' : ''}
                  />
                  {errores.correo && <span className="error-message">{errores.correo}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="telefono">Teléfono (Opcional)</label>
                  <input
                    id="telefono"
                    type="tel"
                    name="telefono"
                    placeholder="987654321"
                    value={nuevoUsuario.telefono}
                    onChange={handleInputChange}
                  />
                </div>
              </div>

              {/* Columna Derecha */}
              <div className="form-column-register">
                <div className="form-group"> 
                  <label htmlFor="apellido">Apellidos</label> {/* Cambiado htmlFor */}
                  <input
                    id="apellido" // Cambiado id
                    type="text"
                    name="apellido" // Cambiado name
                    placeholder="Ingrese los apellidos"
                    value={nuevoUsuario.apellido}
                    onChange={handleInputChange}
                    className={errores.apellido ? 'input-error' : ''}
                  />
                  {errores.apellido && <span className="error-message">{errores.apellido}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="contraseña">Contraseña</label>
                  <input
                    id="contraseña"
                    type="password"
                    name="contraseña"
                    placeholder="••••••••"
                    value={nuevoUsuario.contraseña}
                    onChange={handleInputChange}
                    className={errores.contraseña ? 'input-error' : ''}
                  />
                  {errores.contraseña && <span className="error-message">{errores.contraseña}</span>}
                </div>

                <div className="form-group">
                  <label htmlFor="direccion">Dirección (Opcional)</label>
                  <input
                    id="direccion"
                    type="text"
                    name="direccion"
                    placeholder="Av. Ejemplo 123"
                    value={nuevoUsuario.direccion}
                    onChange={handleInputChange}
                  />
                </div>
              </div>
            </div>

            <div className="form-group-full-width">
              <label htmlFor="id_rol">Rol del Usuario</label>
              <select
                id="id_rol"
                name="id_rol"
                value={nuevoUsuario.id_rol}
                onChange={handleInputChange}
                className={errores.id_rol ? 'input-error' : ''}
              >
                <option value="">-- Seleccione un rol --</option>
                <option value="2">VENDEDOR</option>
                <option value="1">ADMINISTRADOR</option>
              </select>
              {errores.id_rol && <span className="error-message">{errores.id_rol}</span>}
            </div>

            {errores.form && <div className="form-error-banner">{errores.form}</div>}

            <div className="form-buttons-register">
              <button type="button" className="btn-cancelar" onClick={() => navigate('/admin/dashboard')}>
                Cancelar
              </button>
              <button type="submit" className="btn-guardar" disabled={loading}>
                {loading ? 'Guardando...' : 'Guardar Usuario'}
              </button>
            </div>
          </form>
        </div>
      </div>
      <Footer />
    </div>
  );
};

export default RegistrarUsuario;