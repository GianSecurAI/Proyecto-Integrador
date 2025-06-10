import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';

const RegistrarUsuario = () => {
  const [nombreCompleto, setNombreCompleto] = useState('');
  const [correo, setCorreo] = useState('');
  const [password, setPassword] = useState('');
  const [telefono, setTelefono] = useState('');
  const [direccion, setDireccion] = useState('');
  const [rol, setRol] = useState('VENDEDOR'); // Valor por defecto
  const navigate = useNavigate();

  // Obtener usuario logueado
  const user = JSON.parse(localStorage.getItem('user'));

  // Solo permitir acceso si es ADMINISTRADOR
  if (!user || user.role !== "ADMINISTRADOR") {
    return <div>No tienes permisos para acceder a esta página.</div>;
  }

  const handleSubmit = async (e) => {
    e.preventDefault();
    const nuevoUsuario = {
      nombreCompleto,
      correo,
      password,
      telefono,
      direccion,
      estado: "activo",
      id_rol: rol === "VENDEDOR" ? 2 : 1,
      role: rol
    };

    try {
      const response = await fetch('http://localhost:3001/auth/user/create', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(nuevoUsuario)
      });

      if (response.ok) {
        alert('Usuario registrado correctamente');
        setNombreCompleto('');
        setCorreo('');
        setPassword('');
        setTelefono('');
        setDireccion('');
        setRol('VENDEDOR');
        navigate('/admin/dashboard'); // O la ruta que prefieras
      } else {
        const error = await response.text();
        alert('Error al registrar usuario: ' + error);
      }
    } catch (error) {
      alert('Error de red: ' + error);
    }
  };

  return (
    <div>
      <h2>Registrar Vendedor o Administrador</h2>
      <form onSubmit={handleSubmit}>
        <div>
          <label>Nombre completo:</label>
          <input type="text" value={nombreCompleto} onChange={e => setNombreCompleto(e.target.value)} required />
        </div>
        <div>
          <label>Correo:</label>
          <input type="email" value={correo} onChange={e => setCorreo(e.target.value)} required />
        </div>
        <div>
          <label>Contraseña:</label>
          <input type="password" value={password} onChange={e => setPassword(e.target.value)} required />
        </div>
        <div>
          <label>Teléfono:</label>
          <input type="text" value={telefono} onChange={e => setTelefono(e.target.value)} />
        </div>
        <div>
          <label>Dirección:</label>
          <input type="text" value={direccion} onChange={e => setDireccion(e.target.value)} />
        </div>
        <div>
          <label>Rol:</label>
          <select value={rol} onChange={e => setRol(e.target.value)}>
            <option value="VENDEDOR">Vendedor</option>
            <option value="ADMINISTRADOR">Administrador</option>
          </select>
        </div>
        <button type="submit">Registrar Usuario</button>
      </form>
    </div>
  );
};

export default RegistrarUsuario;