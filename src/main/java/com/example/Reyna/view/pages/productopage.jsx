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
  const [productData, setProductData] = useState(null);

  // Catálogo de productos (en un proyecto real, esto vendría de una API o base de datos)
  const productCatalog = {
    'cielo-rosa': {
      image: prodFrag2Img,
      title: 'CIELO EN ROSA EAU DE PARFUM',
      volume: '50.00ml',
      price: 'S/ 113.00',
      description:
        'Un nuevo perfume de la línea Cielo que te invita a asombrarte con su belleza. Cielo en Rosa trae toda la magia floral de la rosa búlgara en una nueva interpretación moderna y llena de contrastes, con almendra tostada y almizcle dulce.',
      ingredients: [
        {
          name: 'Rosa búlgara',
          description:
            'Una nota floral impactante con toques dulces como de miel, frutales como de liche y un aire fresco como el rocío de la mañana.',
        },
        {
          name: 'Almendra tostada',
          description:
            'Una nota innovadora que le agrega carácter al perfume. Su aroma es cálido y suave con textura aterciopelada, dulce y con toques quemados.',
        },
        {
          name: 'Almizcle dulce',
          description:
            'O musk dulce, es una nota cálida y sensual que aporta sofisticación y suavidad al aroma. Promueve la duración y potencia las demás notas del perfume.',
        },
      ],
    },
    'ccori-rose': {
      image: prodFrag1Img,
      title: 'SET CCORI ROSÉ: PARFUM + LOCIÓN PERFUMADA',
      volume: '75.00ml + 150.00ml',
      price: 'S/ 119.00',
      description:
        'Este exclusivo set de Ccori Rosé combina un elegante parfum con una lujosa loción perfumada para una experiencia sensorial completa. La fragancia combina notas florales y frutales con un fondo cálido y sensual que perdura durante todo el día.',
      ingredients: [
        {
          name: 'Notas de salida: Bergamota y Melocotón',
          description:
            'Una combinación refrescante y dulce que abre la experiencia olfativa con un toque frutal vibrante y enérgico.',
        },
        {
          name: 'Notas de corazón: Rosa de Damasco y Jazmín',
          description:
            'El corazón floral aporta feminidad y romanticismo, con la rosa aportando elegancia y el jazmín añadiendo un toque exótico y sensual.',
        },
        {
          name: 'Notas de fondo: Vainilla y Pachulí',
          description:
            'La base cálida y envolvente proporciona profundidad y duración, con la vainilla aportando dulzura cremosa y el pachulí añadiendo una sensualidad terrosa.',
        },
      ],
    },
    'sauvage-dior': {
      image: prodFrag3Img,
      title: 'SET SAUVAGE DIOR: PARFUM + ESTUCHE ELEGANTE',
      volume: '100.00ml',
      price: 'S/ 105.00',
      description:
        'El set Sauvage Dior combina el icónico perfume con un estuche elegante para regalo. Una fragancia masculina potente y noble, inspirada en paisajes desérticos bajo cielos azules. Sauvage es una composición de frescura vibrante con matices amaderados y especiados.',
      ingredients: [
        {
          name: 'Bergamota de Calabria',
          description:
            'Una cítrica jugosa y fresca que aporta luminosidad y un carácter vibrante a la composición.',
        },
        {
          name: 'Pimienta de Sichuan',
          description:
            'Especiada y ligeramente cítrica, añade un toque chispeante y sofisticado que hace que la fragancia sea única.',
        },
        {
          name: 'Ambroxan',
          description:
            'Derivado del ámbar gris, aporta profundidad y un rastro amaderado sensual que perdura en la piel durante horas.',
        },
      ],
    },
    'prod-frag4': {
      image: prodFrag4Img,
      title: 'Fragancia Intensa para Hombre',
      volume: '100ml',
      price: 'S/ 150.00',
      description:
        'Una fragancia audaz y cautivadora para el hombre moderno. Con notas amaderadas y especiadas, este perfume deja una impresión duradera y sofisticada, perfecta para cualquier ocasión.',
      ingredients: [
        {
          name: 'Madera de Cedro',
          description:
            'Aporta una base robusta y masculina, evocando fuerza y estabilidad.',
        },
        {
          name: 'Pimienta Negra',
          description:
            'Introduce un toque picante y energizante, añadiendo vitalidad a la fragancia.',
        },
        {
          name: 'Cuero',
          description:
            'Una nota rica y envolvente que confiere un carácter lujoso y distintivo.',
        },
      ],
    },
    'prod-frag5': {
      image: prodFrag5Img,
      title: 'Una Instinct Eau de Parfum',
      volume: '75ml',
      price: 'S/ 165.00',
      description:
        'Despierta tus instintos con Una Instinct. Una fragancia chipre frutal que combina la dulzura de las frutas rojas con la intensidad del pachulí y la elegancia de la gardenia. Para una mujer auténtica y poderosa.',
      ingredients: [
        {
          name: 'Cassis y Frutos Rojos',
          description:
            'Una explosión jugosa y vibrante que aporta un toque frutal adictivo.',
        },
        {
          name: 'Gardenia',
          description:
            'Una flor blanca sofisticada y femenina que añade elegancia y luminosidad.',
        },
        {
          name: 'Pachulí y Priprioca',
          description:
            'Notas amaderadas y terrosas que otorgan profundidad, misterio y un toque brasileño único.',
        },
      ],
    },    'prod-frag6': {
      image: prodFrag6Img,
      title: 'Bombshell Seduction Eau de Parfum',
      volume: '50ml',
      price: 'S/ 180.00',
      description:
        'Descubre la seducción en su forma más pura. Bombshell Seduction es una fragancia floral oriental que envuelve la piel con un velo de misterio y sensualidad. Ideal para la mujer que no teme destacar.',
      ingredients: [
        {
          name: 'Tuberosa Blanca',
          description:
            'Una flor opulenta y cremosa que aporta una feminidad embriagadora.',
        },
        {
          name: 'Salvia',
          description:
            'Un toque aromático y herbal que añade frescura y un contraste intrigante.',
        },
        {
          name: 'Almizcle Aterciopelado',
          description:
            'Proporciona una calidez sensual y una estela duradera y adictiva.',
        },
      ],
    },
    'euforia-floral': {
      image: prodFrag7Img,
      title: 'EUFORIA FLORAL COLLECTION',
      volume: '75ml',
      price: 'S/ 145.00',
      description:
        'Euforia Floral Collection es una explosión de aromas florales mezclados con dulces notas frutales. Una fragancia diseñada para mujeres que desean expresar su feminidad a través de un aroma juvenil y vibrante que dura todo el día.',
      ingredients: [
        {
          name: 'Peonía Rosa',
          description:
            'Flor con un aroma dulce y delicado que aporta frescura y un toque romántico a la composición.',
        },
        {
          name: 'Frambuesa Silvestre',
          description:
            'Añade un elemento frutal jugoso y ligeramente ácido que equilibra perfectamente las notas florales.',
        },
        {
          name: 'Vainilla de Madagascar',
          description:
            'Proporciona una base cálida y envolvente que perdura en la piel con un dulzor sutil y elegante.',
        },
      ],
    },
    'elegance-set': {
      image: prodFrag8Img,
      title: 'SET ELEGANCE: PERFUME + BODY LOTION',
      volume: '100ml + 200ml',
      price: 'S/ 135.00',
      description:
        'El Set Elegance combina un sofisticado perfume con una lujosa loción corporal para una experiencia sensorial completa. La fragancia fusiona notas frutales y florales sobre una base amaderada que perdura elegantemente en la piel.',
      ingredients: [
        {
          name: 'Flor de Naranjo',
          description:
            'Aporta un aroma cítrico y floral con una dulzura natural que evoca frescura y pureza.',
        },
        {
          name: 'Iris y Jazmín',
          description:
            'Un corazón floral sofisticado que combina la elegancia del iris con la sensualidad del jazmín.',
        },
        {
          name: 'Sándalo Cremoso',
          description:
            'Una base amaderada suave que proporciona calidez y profundidad al perfume.',
        },
      ],
    },
    'midnight-dreams': {
      image: prodFrag9Img,
      title: 'MIDNIGHT DREAMS EAU DE PARFUM',
      volume: '90ml',
      price: 'S/ 190.00',
      description:
        'Midnight Dreams es una fragancia misteriosa y seductora inspirada en la magia de la noche. Sus acordes orientales y especiados crean un aura de misterio y sofisticación, perfecta para ocasiones especiales y veladas románticas.',
      ingredients: [
        {
          name: 'Pimienta Rosa',
          description:
            'Una nota picante y chispeante que despierta los sentidos con su carácter vibrante y energético.',
        },
        {
          name: 'Rosa de Damasco',
          description:
            'El corazón de la fragancia, aportando una feminidad intensa con matices de miel y especias.',
        },
        {
          name: 'Ámbar y Oud',
          description:
            'Una combinación exótica que proporciona profundidad, calidez y un rastro memorable con toques resinosos y amaderados.',
        },
      ],
    },
    'sweet-garden': {
      image: prodFrag10Img,
      title: 'SWEET GARDEN COLLECTION',
      volume: '60ml',
      price: 'S/ 170.00',
      description:
        'Sweet Garden te transporta a un jardín en plena floración con su bouquet de flores frescas y frutas jugosas. Una fragancia alegre y luminosa que evoca la primavera y el optimismo, perfecta para el uso diario.',
      ingredients: [
        {
          name: 'Pera Verde y Manzana',
          description:
            'Un dúo frutal refrescante que aporta jugosidad, vivacidad y un toque verde a la composición.',
        },
        {
          name: 'Magnolia y Lirio de los Valles',
          description:
            'Notas florales delicadas que crean un corazón romántico y femenino con un aroma limpio y cristalino.',
        },
        {
          name: 'Ámbar Blanco',
          description:
            'Una base suave y envolvente que añade calidez y longevidad a la fragancia sin resultar abrumadora.',
        },
      ],
    },
    'ocean-breeze': {
      image: prodFrag11Img,
      title: 'OCEAN BREEZE FOR MEN',
      volume: '100ml',
      price: 'S/ 155.00',
      description:
        'Ocean Breeze captura la frescura indomable del mar con notas acuáticas y aromáticas. Una fragancia masculina vigorosa y refrescante que evoca libertad y aventura, ideal para el hombre moderno y dinámico.',
      ingredients: [
        {
          name: 'Bergamota y Notas Marinas',
          description:
            'Una apertura fresca y energizante que recuerda a la brisa marina y los cítricos bañados por el sol.',
        },
        {
          name: 'Salvia y Lavanda',
          description:
            'Un corazón aromático que aporta masculinidad y frescura herbal, con un toque mediterráneo.',
        },
        {
          name: 'Almizcle y Madera de Cedro',
          description:
            'Una base potente y duradera que añade profundidad y una sensualidad sutil a la composición.',
        },
      ],
    },
    'diamond-rose': {
      image: prodFrag12Img,
      title: 'DIAMOND ROSE LIMITED EDITION',
      volume: '50ml',
      price: 'S/ 210.00',
      description:
        'Diamond Rose es una creación exclusiva de edición limitada que celebra la máxima feminidad. Esta joya olfativa combina la más preciada rosa búlgara con ingredientes de lujo para crear una fragancia sofisticada, intensa y memorable.',
      ingredients: [
        {
          name: 'Champagne y Frambuesa',
          description:
            'Un acorde efervescente y frutal que aporta elegancia y un toque festivo a la composición.',
        },
        {
          name: 'Rosa Búlgara y Peonía',
          description:
            'El corazón de la fragancia, con la rosa más preciada del mundo perfumístico combinada con la suavidad de la peonía.',
        },
        {
          name: 'Pachulí y Vainilla Bourbon',
          description:
            'Una base opulenta y adictiva que deja una estela irresistible, con la profundidad del pachulí y la cremosidad de la vainilla.',
        },
      ],
    },
  };

  // Obtener los datos del producto según la ID de la URL
  useEffect(() => {
    if (id && productCatalog[id]) {
      setProductData(productCatalog[id]);
    } else {
      // Manejo de producto no encontrado
      console.error('Producto no encontrado');
      // Aquí podrías redirigir a una página 404 o a la página de productos
    }
  }, [id]);

  if (!productData) {
    return (
      <div className="page-container-for-fixed-nav">
        <Navbar />
        <div className="container py-5 text-center">
          <h2>Cargando producto...</h2>
          {/* O mostrar mensaje de error si el producto no existe */}
        </div>
        <Footer />
      </div>
    );
  }

  return (
    <div className="page-container-for-fixed-nav">
      <Navbar />
      <ProductDetail {...productData} />
      <Footer />
    </div>
  );
};

export default ProductoPage;