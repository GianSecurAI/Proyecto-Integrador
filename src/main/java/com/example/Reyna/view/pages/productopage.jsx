import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Footer from '../components/Footer';
import ProductDetail from '../components/ProductDetail';
import prodFrag1Img from '../../../../../../../assets/img/prod-frag1.png';
import prodFrag2Img from '../../../../../../../assets/img/prod-frag2.png';
import prodFrag3Img from '../../../../../../../assets/img/prod-frag3.png';
import prodFrag4Img from '../../../../../../../assets/img/prod-frag4.jpg';
import prodFrag5Img from '../../../../../../../assets/img/prod-frag5.jpg';
import prodFrag6Img from '../../../../../../../assets/img/prod-frag6.png';
import prodFrag7Img from '../../../../../../../assets/img/prod-frag7.png';
import prodFrag8Img from '../../../../../../../assets/img/prod-frag8.png';
import prodFrag9Img from '../../../../../../../assets/img/prod-frag9.png';
import prodFrag10Img from '../../../../../../../assets/img/prod-frag10.png';
import prodFrag11Img from '../../../../../../../assets/img/prod-frag11.png';
import prodFrag12Img from '../../../../../../../assets/img/prod-frag12.png';

const ProductoPage = () => {
  const { id } = useParams();
  const [apiProduct, setApiProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Mapeo de imágenes (temporal hasta que las imágenes vengan de la API)
  const productImages = {
    'P001': prodFrag1Img,
    'P002': prodFrag2Img,
    'P003': prodFrag3Img,
    'P004': prodFrag4Img,
    'P005': prodFrag5Img,
    'P006': prodFrag6Img,
    'P007': prodFrag7Img,
    'P008': prodFrag8Img,
    'P009': prodFrag9Img,
    'P010': prodFrag10Img,
    'P011': prodFrag11Img,
    'P012': prodFrag12Img,
  };

  useEffect(() => {
    if (!id) return;

    setLoading(true);
    fetch(`/api/productos/${id}`)
      .then(response => {
        if (!response.ok) {
          throw new Error('Producto no encontrado');
        }
        return response.json();
      })
      .then(data => {
        setApiProduct(data);
        setError(null);
      })
      .catch(err => {
        setError(err.message);
        setApiProduct(null);
      })
      .finally(() => {
        setLoading(false);
      });
  }, [id]);

  if (loading) {
    return (
      <div className="page-container-for-fixed-nav">
        <Navbar />
        <div className="container py-5 text-center">
          <h2>Cargando producto...</h2>
        </div>
        <Footer />
      </div>
    );
  }

  if (error) {
    return (
      <div className="page-container-for-fixed-nav">
        <Navbar />
        <div className="container py-5 text-center">
          <h2>Error</h2>
          <p>{error}</p>
        </div>
        <Footer />
      </div>
    );
  }

  if (!apiProduct) {
    // Esto puede pasar si el ID no es válido pero no hubo un error de red
    return null; 
  }

  // Adaptar los datos de la API al formato que espera ProductDetail
  const productDetailProps = {
    image: productImages[apiProduct.codigo] || prodFrag5Img, // Imagen por defecto
    title: apiProduct.nombre_producto,
    volume: apiProduct.contenido,
    price: `S/ ${apiProduct.precio.toFixed(2)}`,
    description: apiProduct.descripcion,
    ingredients: [], // La API actual no provee ingredientes
  };

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      <ProductDetail {...productDetailProps} />
      <Footer />
    </div>
  );
};

export default ProductoPage;