package cl.duoc.tienda.config;

import cl.duoc.tienda.model.Categoria;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.CategoriaRepository;
import cl.duoc.tienda.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DatosIniciales {

    @Bean
    CommandLineRunner catalogoInicial(CategoriaRepository catRepo, ProductoRepository prodRepo) {
        return args -> {
            if (catRepo.count() > 0) {
                return;
            }

            Categoria cuerdas = crearCategoria(catRepo, "Cuerdas", "Guitarras eléctricas, acústicas y bajos");
            Categoria percusion = crearCategoria(catRepo, "Percusión", "Baterías acústicas y electrónicas");
            Categoria audio = crearCategoria(catRepo, "Audio", "Auriculares de estudio y monitoreo");
            Categoria microfonos = crearCategoria(catRepo, "Micrófonos", "Micrófonos de condensador y dinámicos");

            crearProducto(prodRepo, "Guitarra Eléctrica Negra",
                "Guitarra eléctrica de cuerpo sólido con pastillas humbucker y acabado negro brillante.",
                249990, 8, "guitarra", cuerdas);

            crearProducto(prodRepo, "Batería Acústica Profesional",
                "Set completo de 5 cuerpos con platillos de bronce y atriles reforzados.",
                599990, 4, "bateria", percusion);

            crearProducto(prodRepo, "Auriculares de Estudio",
                "Monitoreo plano de alta fidelidad con aislamiento pasivo para mezcla y grabación.",
                89990, 15, "auriculares", audio);

            crearProducto(prodRepo, "Micrófono Condensador",
                "Micrófono cardioide de diafragma grande ideal para voces e instrumentos acústicos.",
                119990, 10, "microfono", microfonos);
        };
    }

    private Categoria crearCategoria(CategoriaRepository repo, String nombre, String descripcion) {
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(descripcion);
        return repo.save(c);
    }

    private void crearProducto(ProductoRepository repo, String nombre, String descripcion,
                               Integer precio, Integer stock, String imagen, Categoria categoria) {
        Producto p = new Producto();
        p.setNombre(nombre);
        p.setDescripcion(descripcion);
        p.setPrecio(precio);
        p.setStock(stock);
        p.setImagen(imagen);
        p.setCategoria(categoria);
        repo.save(p);
    }
}