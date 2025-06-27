// apiClient.js
const API_BASE_URL = 'http://localhost:8080'; // Asegúrate de que esta URL sea correcta para tu backend

const apiClient = async (endpoint, options = {}) => {
  const token = localStorage.getItem('token'); // Obtiene el token del almacenamiento local

  const headers = {
    'Content-Type': 'application/json',
    ...options.headers, // Permite sobrescribir o añadir otros headers
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`; // Añade el token JWT al encabezado de autorización
  }

  const response = await fetch(`${API_BASE_URL}${endpoint}`, {
    ...options,
    headers: headers,
  });
  return response;
};

export default apiClient;
