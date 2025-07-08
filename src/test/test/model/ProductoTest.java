@DataJpaTest
public class ProductoTest {

    @Autowired
    private Producto productoRepository;

    @Test
    void testGuardarProductoSimple() {
        Producto producto = new Producto();
        producto.setNombre_producto("TV Samsung");
        producto.setCodigo("TV-001");
        producto.setPrecio(1200.0);
        producto.setStock(5);
        producto.setMarca("Samsung");
        producto.setEstado(true);
        producto.setContenido("50 pulgadas");
        producto.setDescripcion("TV LED 4K UHD");

        Producto guardado = productoRepository.save(producto);

        assertThat(guardado.getId_producto()).isNotNull();
        assertThat(guardado.getNombre_producto()).isEqualTo("TV Samsung");
    }
}
