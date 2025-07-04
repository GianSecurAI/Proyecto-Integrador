import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import Footer from '../../components/Footer';
import '../../styles/ProductosAdmin.css';
import apiClient from '../../../api/apiClient'; // Asegúrate de que la ruta es correcta
// Importa las librerías necesarias para exportar a Excel
import * as XLSX from 'xlsx';
import { saveAs } from 'file-saver';

const ProductosAdmin = () => {
  const navigate = useNavigate();
  const [productos, setProductos] = useState([]);
  const [categoriaSeleccionada, setCategoriaSeleccionada] = useState('Todos');
  const [productosFiltrados, setProductosFiltrados] = useState([]);
  const [mostrarModal, setMostrarModal] = useState(false);
  const [nuevoProducto, setNuevoProducto] = useState({
    nombre: '',
    categoria: '',
    marca: '',
    contenido: '',
    descripcion: '',
    codigo: '',
    stock: '',
  });
  const [errores, setErrores] = useState({});
  const [mostrarModalBusqueda, setMostrarModalBusqueda] = useState(false);
  const [mostrarModalResultado, setMostrarModalResultado] = useState(false);
  const [isEditing, setIsEditing] = useState(false);
  const [busquedaProducto, setBusquedaProducto] = useState({
    categoria: '',
    codigo: ''
  });
  const [productoEncontrado, setProductoEncontrado] = useState(null);

  const categorias = ['Todos', 'Damas', 'Caballeros', 'Niños', 'Unisex'];
  const marcas = ['Dior', 'Carolina Herrera', 'Lancôme', 'MAC', 'L\'Oreal', 'Maybelline'];

  useEffect(() => {
    cargarProductos();
  }, []);

  useEffect(() => {
    if (categoriaSeleccionada === 'Todos') {
      setProductosFiltrados(productos);
    } else {
      setProductosFiltrados(
        productos.filter(p => p.categoria === categoriaSeleccionada)
      );
    }
  }, [categoriaSeleccionada, productos]);

  const user = JSON.parse(localStorage.getItem('user'));

  const cargarProductos = async () => {
    console.log('Intentando cargar productos...');
    try {
      const response = await apiClient('/api/productos');
      if (!response.ok) {
        throw new Error(`Error al cargar productos: ${response.status} ${response.statusText}`);
      }
      const data = await response.json();
      console.log('Datos recibidos del backend:', data);
      const productosMapeados = data.map(p => ({
        id: p.id_producto,
        nombre: p.nombre_producto,
        categoria: p.categoria ? p.categoria.nombre_categoria : '',
        precio: p.precio,
        codigo: p.codigo,
        marca: p.marca,
        stock: p.stock,
        contenido: p.contenido,
        descripcion: p.descripcion,
      }));
      setProductos(productosMapeados);
    } catch (error) {
      console.error('Error al cargar productos:', error.message);
      alert(error.message);
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setNuevoProducto(prev => ({ ...prev, [name]: value }));
  };

  const handleEditedProductChange = (e) => {
    const { name, value } = e.target;
    setProductoEncontrado(prev => ({ ...prev, [name]: value }));
  };

  if (!user || !user.rol || (user.rol.nombre !== 'ADMINISTRADOR' && user.rol.nombre !== 'VENDEDOR')) {
    return <div>No tienes permisos para acceder a esta página.</div>;
  }

  const handleAgregarClick = () => {
    setMostrarModal(true);
  };
  const handleCancelarClick = () => {
    setMostrarModal(false);
    setNuevoProducto({
      nombre: '', categoria: '', marca: '', precio: '',
      codigo: '', contenido: '', descripcion: '', stock: ''
    });
    setErrores({});
  };

  const validarFormulario = () => {
    const nuevosErrores = {};
    if (!nuevoProducto.nombre.trim()) nuevosErrores.nombre = 'El nombre es requerido';
    if (!nuevoProducto.categoria) nuevosErrores.categoria = 'La categoría es requerida';
    if (!nuevoProducto.marca) nuevosErrores.marca = 'La marca es requerida';
    if (!nuevoProducto.precio || nuevoProducto.precio <= 0) nuevosErrores.precio = 'El precio debe ser mayor a 0';
    if (!nuevoProducto.stock || nuevoProducto.stock <= 0) nuevosErrores.stock = 'El stock debe ser mayor a 0';
    
    const codigoRegex = /^P\d{3}$/;
    if (!nuevoProducto.codigo.trim()) {
      nuevosErrores.codigo = 'El código es requerido';
    } else if (!codigoRegex.test(nuevoProducto.codigo)) {
      nuevosErrores.codigo = 'El código debe tener el formato P001, P002, etc.';
    } else if (productos.some(p => p.codigo === nuevoProducto.codigo && (!productoEncontrado || p.id !== productoEncontrado.id))) {
      nuevosErrores.codigo = 'Este código ya existe';
    }
    setErrores(nuevosErrores);
    return Object.keys(nuevosErrores).length === 0;
  };

  const handleGuardarProducto = async () => {
    if (validarFormulario()) {
      const categoriaId = categorias.indexOf(nuevoProducto.categoria);
      const nuevoProductoFormateado = {
        nombre_producto: nuevoProducto.nombre,
        contenido: nuevoProducto.contenido || '',
        descripcion: nuevoProducto.descripcion || '',
        precio: Number(nuevoProducto.precio),
        stock: Number(nuevoProducto.stock),
        categoria: { id_categoria: categoriaId },
        estado: true,
        marca: nuevoProducto.marca,
        codigo: nuevoProducto.codigo,
      };

      try {
        const response = await apiClient('/api/productos', {
          method: 'POST',
          body: JSON.stringify(nuevoProductoFormateado)
        });
        if (!response.ok) throw new Error('Error al guardar el producto');
        await cargarProductos();
        handleCancelarClick();
      } catch (error) {
        console.error('Error al guardar producto:', error);
        alert('No se pudo guardar el producto');
      }
    }
  };

  const handleBuscarClick = () => {
    setMostrarModalBusqueda(true);
    setIsEditing(false);
  };

  const handleCancelarBusqueda = () => {
    setMostrarModalBusqueda(false);
    setBusquedaProducto({ categoria: '', codigo: '' });
  };

  const handleBuscarProducto = () => {
    const producto = productos.find(p => p.codigo === busquedaProducto.codigo);
    if (producto) {
      setProductoEncontrado(producto);
      setMostrarModalBusqueda(false);
      setMostrarModalResultado(true);
    } else {
      alert('Producto no encontrado.');
    }
  };

  const handleVolverABusqueda = () => {
    setMostrarModalResultado(false);
    setMostrarModalBusqueda(true);
    setIsEditing(false);
  };

  const handleCerrarResultado = () => {
    setMostrarModalResultado(false);
    setProductoEncontrado(null);
    setIsEditing(false);
  };

  const handleEditClick = () => {
    setIsEditing(true);
  };

  const handleSaveEditedProduct = async () => {
    if (!productoEncontrado) return;

    if (!productoEncontrado.nombre.trim() || !productoEncontrado.precio || Number(productoEncontrado.precio) <= 0 || !productoEncontrado.stock || Number(productoEncontrado.stock) < 0) {
      alert('Por favor, complete todos los campos requeridos. El precio y el stock deben ser números válidos.');
      return;
    }

    const categoriaId = categorias.indexOf(productoEncontrado.categoria);
    const productoParaActualizar = {
      nombre_producto: productoEncontrado.nombre,
      precio: Number(productoEncontrado.precio),
      stock: Number(productoEncontrado.stock),
      marca: productoEncontrado.marca,
      codigo: productoEncontrado.codigo,
      contenido: productoEncontrado.contenido,
      descripcion: productoEncontrado.descripcion,
      categoria: { id_categoria: categoriaId },
      estado: true,
    };

    try {
      const response = await apiClient(`/api/productos/${productoEncontrado.id}`, {
        method: 'PUT',
        body: JSON.stringify(productoParaActualizar)
      });
      if (!response.ok) throw new Error('Error al actualizar el producto');
      alert('Producto actualizado con éxito');
      await cargarProductos();
      handleCerrarResultado();
    } catch (error) {
      console.error('Error al guardar el producto editado:', error);
      alert('No se pudo guardar los cambios del producto.');
    }
  };

  const handleDeleteProduct = async () => {
    if (!productoEncontrado) return;
    if (window.confirm(`¿Estás seguro de que quieres eliminar el producto "${productoEncontrado.nombre}"?`)) {
      try {
        const response = await apiClient(`/api/productos/${productoEncontrado.id}`, {
          method: 'DELETE',
        });
        if (!response.ok) throw new Error('Error al eliminar el producto');
        alert('Producto eliminado con éxito');
        await cargarProductos();
        handleCerrarResultado();
      } catch (error) {
        console.error('Error al eliminar el producto:', error);
        alert('No se pudo eliminar el producto.');
      }
    }
  };

  const handleSalir = () => {
    navigate('/admin/dashboard');
  };

  const exportarExcel = () => {
    const data = productosFiltrados.map(({ codigo, nombre, categoria, precio }) => ({
      'Código': codigo,
      'Producto': nombre,
      'Categoría': categoria,
      'Precio': `S/. ${precio.toFixed(2)}`
    }));
    const worksheet = XLSX.utils.json_to_sheet(data);
    const workbook = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(workbook, worksheet, 'Productos');
    const excelBuffer = XLSX.write(workbook, { bookType: 'xlsx', type: 'array' });
    const blob = new Blob([excelBuffer], { type: 'application/octet-stream' });
    saveAs(blob, 'productos.xlsx');
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      
      <div className="productos-admin-container">
        <div className="productos-header">
          <h1>Mantenimiento de Productos</h1>
          <div className="productos-acciones">
            <button className="btn-accion btn-agregar" onClick={handleAgregarClick}>AGREGAR PRODUCTO</button>
            <button className="btn-accion btn-buscar" onClick={handleBuscarClick}>BUSCAR PRODUCTO</button>
            <button className="btn-accion btn-salir" onClick={handleSalir}>SALIR</button>
          </div>
        </div>

        {/* Modal de Nuevo Producto */}
        {mostrarModal && (
          <div className="modal-overlay">
            <div className="modal-content">              <h2>Nuevo Producto</h2>
              <hr className="divisor" />
              <div className="form-grid">
                <div className="form-column">
                  <div className="form-group">
                    <label>Nombre Producto</label>
                    <input type="text" name="nombre" placeholder="Ingrese nombre del producto" value={nuevoProducto.nombre} onChange={handleInputChange} className={errores.nombre ? 'input-error' : ''} />
                    {errores.nombre && <span className="error-message">{errores.nombre}</span>}
                  </div>
                  <div className="form-group">
                    <label>Categoría</label>
                    <select name="categoria" value={nuevoProducto.categoria} onChange={handleInputChange} className={errores.categoria ? 'input-error' : ''}>
                      <option value="">Seleccione categoría</option>
                      {categorias.slice(1).map(cat => (<option key={cat} value={cat}>{cat}</option>))}
                    </select>
                    {errores.categoria && <span className="error-message">{errores.categoria}</span>}
                  </div>
                  <div className="form-group">
                    <label>Marca</label>
                    <select name="marca" value={nuevoProducto.marca} onChange={handleInputChange} className={errores.marca ? 'input-error' : ''}>
                      <option value="">Seleccione marca</option>
                      {marcas.map(marca => (<option key={marca} value={marca}>{marca}</option>))}
                    </select>
                    {errores.marca && <span className="error-message">{errores.marca}</span>}
                  </div>
                  {/* Campos de Contenido y Descripción para Nuevo Producto */}
                  <div className="form-group">
                    <label>Contenido</label>
                    <input type="text" name="contenido" placeholder="Ej: 100ml" value={nuevoProducto.contenido || ''} onChange={handleInputChange} />
                  </div>
                  <div className="form-group">
                    <label>Descripción</label>
                    <input type="text" name="descripcion" placeholder="Descripción del producto" value={nuevoProducto.descripcion || ''} onChange={handleInputChange} />
                  </div>
                </div>
                <div className="form-column">
                  <div className="form-group">
                    <label>Precio Producto</label>
                    <input type="number" name="precio" placeholder="0.00" value={nuevoProducto.precio || ''} onChange={handleInputChange} className={errores.precio ? 'input-error' : ''} min="0" step="0.01" />
                    {errores.precio && <span className="error-message">{errores.precio}</span>}
                  </div>
                  <div className="form-group">
                    <label>Stock</label>
                    <input type="number" name="stock" placeholder="Cantidad en stock" value={nuevoProducto.stock || ''} onChange={handleInputChange} min="1" className={errores.stock ? 'input-error' : ''} />
                    {errores.stock && <span className="error-message">{errores.stock}</span>}
                  </div>
                  <div className="form-group">
                    <label>Código</label>
                    <input type="text" name="codigo" placeholder="Ingrese código del producto" value={nuevoProducto.codigo || ''} onChange={handleInputChange} className={errores.codigo ? 'input-error' : ''} />
                    {errores.codigo && <span className="error-message">{errores.codigo}</span>}
                  </div>
                </div>
              </div>              <div className="modal-buttons">
                <button onClick={handleCancelarClick} className="btn-cancelar">Cancelar</button>
                <button onClick={handleGuardarProducto} className="btn-guardar">
                  Guardar
                </button>
              </div>
            </div>
          </div>
        )}

        {/* Modal de Búsqueda */}
        {mostrarModalBusqueda && (
          <div className="modal-overlay">
            <div className="modal-content modal-busqueda">
              <div className="modal-header">
                <h2>Buscar Producto</h2>
                <button className="close-button" onClick={handleCancelarBusqueda}>&times;</button>
              </div>
              <div className="form-busqueda">
                <div className="form-group">
                  <label>Código</label>
                  <input type="text" value={busquedaProducto.codigo} onChange={(e) => setBusquedaProducto(prev => ({...prev, codigo: e.target.value}))} placeholder="Ingrese código del producto" />
                </div>
                <div className="modal-buttons">
                  <button onClick={handleCancelarBusqueda} className="btn-cancelar">Cancelar</button>
                  <button onClick={handleBuscarProducto} className="btn-buscar">Buscar</button>
                </div>
              </div>
            </div>
          </div>
        )}

        {/* Modal de Resultado de Búsqueda */}
        {mostrarModalResultado && productoEncontrado && (
          <div className="modal-overlay">
            <div className="modal-content modal-resultado">
              <div className="modal-header">
                <h2>Producto Buscado</h2>
                <button className="close-button" onClick={handleCerrarResultado}>&times;</button>
              </div>
              <div className="form-grid">
                <div className="form-column">
                  <div className="form-group">
                    <label>Nombre Producto</label>
                    <input type="text" name="nombre" value={productoEncontrado.nombre} onChange={handleEditedProductChange} readOnly={!isEditing} />
                  </div>
                  <div className="form-group">
                    <label>Categoría</label>
                    <select name="categoria" value={productoEncontrado.categoria} onChange={handleEditedProductChange} disabled={!isEditing}>
                      {categorias.slice(1).map(cat => (<option key={cat} value={cat}>{cat}</option>))}
                    </select>
                  </div>
                  <div className="form-group">
                    <label>Marca</label>
                    <input type="text" name="marca" value={productoEncontrado.marca || ''} onChange={handleEditedProductChange} readOnly={!isEditing} />
                  </div>
                  {/* Campos de Contenido y Descripción para Edición */}
                  <div className="form-group">
                    <label>Contenido</label>
                    <input type="text" name="contenido" value={productoEncontrado.contenido || ''} onChange={handleEditedProductChange} readOnly={!isEditing} />
                  </div>
                  <div className="form-group">
                    <label>Descripción</label>
                    <input type="text" name="descripcion" value={productoEncontrado.descripcion || ''} onChange={handleEditedProductChange} readOnly={!isEditing} />
                  </div>
                </div>
                <div className="form-column">
                  <div className="form-group">
                    <label>Stock</label>
                    <input type="number" name="stock" value={productoEncontrado.stock} onChange={handleEditedProductChange} readOnly={!isEditing} />
                  </div>
                  <div className="form-group">
                    <label>Precio Producto</label>
                    <input type="number" name="precio" value={productoEncontrado.precio} onChange={handleEditedProductChange} readOnly={!isEditing} step="0.01" /> {/* Asegurar que es tipo number */}
                  </div>
                  <div className="form-group">
                    <label>Código</label>
                    <input type="text" name="codigo" value={productoEncontrado.codigo} readOnly={true} /> {/* El código no debería ser editable */}
                  </div>
                </div>
              </div>
              <div className="modal-buttons busqueda-buttons">
                <button onClick={handleVolverABusqueda} className="btn-volver">
                  <span className="flecha-back">←</span> Volver
                </button>
                <div>
                  <button className="btn-eliminar" onClick={handleDeleteProduct}>Eliminar</button>
                  <button className="btn-editar" onClick={handleEditClick}>Editar</button>
                </div>
                {isEditing && <button className="btn-guardar" onClick={handleSaveEditedProduct}>Guardar Producto</button>}
              </div>
            </div>
          </div>
        )}

        <div className="productos-content">
          <div className="productos-filtros">
            <select value={categoriaSeleccionada} onChange={(e) => setCategoriaSeleccionada(e.target.value)} className="categoria-select">
              {categorias.map(cat => (<option key={cat} value={cat}>{cat}</option>))}
            </select>
            <button className="btn-accion btn-agregar" style={{marginLeft: '1rem'}} onClick={exportarExcel}>
              Exportar a Excel
            </button>
          </div>

          <div className="productos-tabla">
            <table>
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Producto</th>
                  <th>Precio</th>
                </tr>
              </thead>
              <tbody>
                {productosFiltrados.map(producto => (
                  <tr key={producto.id}>
                    <td>{producto.codigo}</td>
                    <td>{producto.nombre}</td>
                    <td>S/. {producto.precio.toFixed(2)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      <Footer />
    </div>
  );
};

export default ProductosAdmin;
