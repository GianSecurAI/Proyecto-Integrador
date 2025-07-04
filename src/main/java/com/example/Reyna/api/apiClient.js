const apiClient = async (url, options = {}) => {
  // Obtiene el token de autenticación desde localStorage.
  // Asegúrate de que la clave 'token' sea la misma que usas al guardar el token en el login.
  const token = localStorage.getItem('token');

  // Configura las cabeceras por defecto.
  const headers = {
    'Content-Type': 'application/json',
    ...options.headers,
  };

  // Si existe un token, lo añade a la cabecera de autorización.
  if (token) {
    headers.Authorization = `Bearer ${token}`;
  }

  const config = {
    ...options,
    headers,
  };

  // La URL base de tu API en el backend.
  const baseUrl = 'http://localhost:8080';

  try {
    const response = await fetch(`${baseUrl}${url}`, config);

    // Si el token es inválido o ha expirado (error 401 o 403),
    // limpia el localStorage y redirige al login para evitar bucles.
    if (response.status === 401 || response.status === 403) {
      localStorage.removeItem('token');
      localStorage.removeItem('id_usuario');
      window.location.href = '/login'; // Redirección forzada para limpiar el estado.
      return new Promise(() => {}); // Detiene la ejecución posterior.
    }

    return response;
  } catch (error) {
    console.error('Error de conexión con el API Client:', error);
    alert('Error de conexión con el servidor. Por favor, intente más tarde.');
    throw error;
  }
};

export default apiClient;