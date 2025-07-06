// filepath: /workspaces/Pruebas-de-codigo/src/pages/login.jsx
import React, { useState, useEffect} from 'react';
import { useNavigate } from 'react-router-dom';
import Footer from '../components/Footer';
import Navbar from '../components/Navbar';
import '../styles/login.css';

const Login = () => {
  const [loginEmail, setLoginEmail] = useState('');
  const [loginPassword, setLoginPassword] = useState('');
  const [registerNombre, setRegisterNombre] = useState(''); // Cambiado de registerName
  const [registerApellido, setRegisterApellido] = useState(''); // Nuevo estado para apellido
  const [registerEmail, setRegisterEmail] = useState('');
  const [registerPassword, setRegisterPassword] = useState('');
  const [registerPhone, setRegisterPhone] = useState('');
  const [registerAddress, setRegisterAddress] = useState('');
  const [showRegister, setShowRegister] = useState(false);
  const [showPassword, setShowPassword] = useState(false);
  const [showRegisterPassword, setShowRegisterPassword] = useState(false);
  const navigate = useNavigate();


  const handleLoginSubmit = async (e) => {
  e.preventDefault();

  try {
    const response = await fetch('http://localhost:8080/auth/login', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        correo: loginEmail,
        contraseña: loginPassword
      })
    });

    let data;
    try {
      data = await response.json();
    } catch {
      data = { message: 'Error inesperado del servidor' };
    }

    if (response.ok) {
      // Para depurar, muestra en la consola la respuesta completa del backend
      console.log("Respuesta del backend:", JSON.stringify(data, null, 2));
      localStorage.setItem('user', JSON.stringify(data.data));
      // --- SOLUCIÓN AL PROBLEMA DEL BUCLE ---
      // El problema es que el ID del usuario no se estaba guardando correctamente.
      // El backend envía 'id_usuario', pero el código buscaba 'id'.
      localStorage.setItem('token', data.token);
      if (data.data && data.data.id_usuario) {
        localStorage.setItem('id_usuario', data.data.id_usuario); // Se guarda como 'id_usuario'
      } else {
        localStorage.removeItem('id_usuario');
      }
      console.log("User data stored in localStorage:", data.data);
      alert(data.message);

      // Redirigir según el nombre del rol para mayor claridad y robustez
      if (data.data && data.data.rol && data.data.rol.nombre === 'ADMINISTRADOR') {
        console.log("Redirigiendo al dashboard de admin...");
        navigate('/admin/dashboard');
      } else {
        console.log("Redirigiendo a la página de inicio...");
        navigate('/');
      }
    } else {
      alert(data.message);
    }
  } catch (error) {
    console.error('Error de red:', error);
    alert('Error al conectar con el servidor');
  }
};

  const handleRegisterSubmit = async (e) => {
    e.preventDefault();
    
    // Validar que el teléfono tenga exactamente 9 dígitos
    if (registerPhone.length !== 9 || !/^\d{9}$/.test(registerPhone)) {
      alert('El número de teléfono debe contener exactamente 9 dígitos numéricos');
      return;
    }
    
    const user = {
      nombre: registerNombre, // Corregido para enviar nombre
      apellido: registerApellido, // Corregido para enviar apellido
      correo: registerEmail,
      contraseña: registerPassword,
      telefono: registerPhone,
      direccion: registerAddress
    };

    try {
      const response = await fetch('http://localhost:8080/auth/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user)
      });

      if (response.ok) {
        const data = await response.json();
        alert(data.message);
        setShowRegister(false);
        setLoginEmail(registerEmail);
      } else {
        const error = await response.text();
        console.error('Error en el registro:', error);
        alert('Error en el registro');
      }
    } catch (error) {
      console.error('Error de red:', error);
      alert('No se pudo conectar al servidor');
    }

    setShowRegister(false);
    setLoginEmail(registerEmail);
    setRegisterNombre(''); // Limpiar estado
    setRegisterApellido(''); // Limpiar estado
    setRegisterEmail('');
    setRegisterPassword('');
    setRegisterPhone('');
    setRegisterAddress('');
  };

  return (
    <div className="login-page-container">
      <Navbar />
      <div className="login-container">
        {!showRegister ? (
          <div className="login-form-container">
            <h2>Acceder</h2>
            <form onSubmit={handleLoginSubmit}>
              <div className="form-group">
                <label htmlFor="login-email">Correo electrónico *</label>
                <input
                  type="email"
                  id="login-email"
                  value={loginEmail}
                  onChange={(e) => setLoginEmail(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="login-password">Contraseña *</label>
                <input
                  type={showPassword ? "text" : "password"}
                  id="login-password"
                  value={loginPassword}
                  onChange={(e) => setLoginPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className="show-password-btn"
                  onClick={() => setShowPassword((prev) => !prev)}
                  tabIndex={-1}
                  aria-label={showPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                >
                  <i className={`bi ${showPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                </button>
              </div>
              <div className="form-options">
                <button type="submit" className="btn-submit">Acceder</button>
                <label className="remember-me">
                  <input type="checkbox" /> Recuérdame
                </label>
              </div>
              <a href="#" className="forgot-password">¿Olvidaste la contraseña?</a>
            </form>
            <div className="switch-form">
              <span>¿No tienes cuenta?</span>
              <button className="btn-link" onClick={() => setShowRegister(true)}>Registrarte</button>
            </div>
          </div>
        ) : (
          <div className="register-form-container">
            <h2>Registrarse</h2>
            <form onSubmit={handleRegisterSubmit}>
              <div className="form-group">
                <label htmlFor="register-nombre">Nombres *</label>
                <input
                  type="text"
                  id="register-nombre"
                  value={registerNombre}
                  onChange={(e) => setRegisterNombre(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="register-apellido">Apellidos *</label>
                <input
                  type="text"
                  id="register-apellido"
                  value={registerApellido}
                  onChange={(e) => setRegisterApellido(e.target.value)}
                  required
                />
              </div>
              <div className="form-group">
                <label htmlFor="register-email">Dirección de correo electrónico *</label>
                <input
                  type="email"
                  id="register-email"
                  value={registerEmail}
                  onChange={(e) => setRegisterEmail(e.target.value)}
                  required
                />
              </div>
              <div className="form-group" style={{ position: 'relative' }}>
                <label htmlFor="register-password">Contraseña *</label>
                <input
                  type={showRegisterPassword ? "text" : "password"}
                  id="register-password"
                  value={registerPassword}
                  onChange={(e) => setRegisterPassword(e.target.value)}
                  required
                />
                <button
                  type="button"
                  className="show-password-btn"
                  onClick={() => setShowRegisterPassword((prev) => !prev)}
                  tabIndex={-1}
                  aria-label={showRegisterPassword ? "Ocultar contraseña" : "Mostrar contraseña"}
                >
                  <i className={`bi ${showRegisterPassword ? 'bi-eye-slash' : 'bi-eye'}`}></i>
                </button>
              </div>              <div className="form-group">
                <label htmlFor="register-phone">Teléfono *</label>
                <input
                  type="tel"
                  id="register-phone"
                  value={registerPhone}
                  onChange={(e) => {
                    // Solo permitir dígitos
                    const value = e.target.value.replace(/\D/g, '');
                    // Limitar a 9 dígitos
                    if (value.length <= 9) {
                      setRegisterPhone(value);
                    }
                  }}
                  pattern="[0-9]{9}"
                  title="El número de teléfono debe contener exactamente 9 dígitos"
                  placeholder="Ingresa 9 dígitos numéricos"
                  required
                />
                {registerPhone && registerPhone.length !== 9 && (
                  <small style={{ color: 'red' }}>El teléfono debe tener exactamente 9 dígitos</small>
                )}
              </div>
              <div className="form-group">
                <label htmlFor="register-address">Dirección</label>
                <input
                  type="text"
                  id="register-address"
                  value={registerAddress}
                  onChange={(e) => setRegisterAddress(e.target.value)}
                />
              </div>
              <p className="privacy-policy">
                Tus datos personales se utilizarán para procesar tu pedido, mejorar
                tu experiencia en esta web, gestionar el acceso a tu cuenta y otros
                propósitos descritos en nuestra política de privacidad.
              </p>
              <button type="submit" className="btn-submit">Registrarse</button>
            </form>
            <div className="switch-form">
              <span>¿Ya tienes cuenta?</span>
              <button className="btn-link" onClick={() => setShowRegister(false)}>Iniciar sesión</button>
            </div>
          </div>
        )}
      </div>
      <Footer />
    </div>
  );
};

export default Login;
