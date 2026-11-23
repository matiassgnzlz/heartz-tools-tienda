package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.ProductoRequest;
import cl.duoc.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.tienda.model.Categoria;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.CategoriaRepository;
import cl.duoc.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

    private final ProductoRepository productoRepo;
    private final CategoriaRepository categoriaRepo;

    public ProductoService(ProductoRepository productoRepo, CategoriaRepository categoriaRepo) {
        this.productoRepo = productoRepo;
        this.categoriaRepo = categoriaRepo;
    }

    public List<Producto> listar(Long categoriaId) {
        if (categoriaId != null) {
            return productoRepo.findByCategoriaId(categoriaId);
        }
        return productoRepo.findAll();
    }

    public Producto buscar(Long id) {
        return productoRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
    }

    public Producto crear(ProductoRequest req) {
        Categoria categoria = categoriaRepo.findById(req.categoriaId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + req.categoriaId()));

        Producto p = new Producto();
        aplicarDatos(p, req, categoria);
        return productoRepo.save(p);
    }

    public Producto actualizar(Long id, ProductoRequest req) {
        Producto actual = buscar(id);
        Categoria categoria = categoriaRepo.findById(req.categoriaId())
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + req.categoriaId()));

        aplicarDatos(actual, req, categoria);
        return productoRepo.save(actual);
    }

    public void eliminar(Long id) {
        productoRepo.delete(buscar(id));
    }

    private void aplicarDatos(Producto p, ProductoRequest req, Categoria categoria) {
        p.setNombre(req.nombre().trim());
        p.setDescripcion(req.descripcion().trim());
        p.setPrecio(req.precio());
        p.setStock(req.stock());
        p.setImagen(req.imagen() != null ? req.imagen().trim() : "");
        p.setCategoria(categoria);
    }
}