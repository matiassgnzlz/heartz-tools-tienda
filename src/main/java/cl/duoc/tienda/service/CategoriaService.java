package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.CategoriaRequest;
import cl.duoc.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.tienda.model.Categoria;
import cl.duoc.tienda.repository.CategoriaRepository;
import cl.duoc.tienda.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

    private final CategoriaRepository categoriaRepo;
    private final ProductoRepository productoRepo;

    public CategoriaService(CategoriaRepository categoriaRepo, ProductoRepository productoRepo) {
        this.categoriaRepo = categoriaRepo;
        this.productoRepo = productoRepo;
    }

    public List<Categoria> listar() {
        return categoriaRepo.findAll();
    }

    public Categoria buscar(Long id) {
        return categoriaRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + id));
    }

    public Categoria crear(CategoriaRequest req) {
        String nombre = req.nombre().trim();
        if (categoriaRepo.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
        }
        Categoria c = new Categoria();
        c.setNombre(nombre);
        c.setDescripcion(req.descripcion() != null ? req.descripcion().trim() : "");
        return categoriaRepo.save(c);
    }

    public Categoria actualizar(Long id, CategoriaRequest req) {
        Categoria actual = buscar(id);
        String nombre = req.nombre().trim();
        if (!actual.getNombre().equalsIgnoreCase(nombre)
                && categoriaRepo.existsByNombreIgnoreCase(nombre)) {
            throw new IllegalArgumentException("Ya existe una categoría con el nombre: " + nombre);
        }
        actual.setNombre(nombre);
        actual.setDescripcion(req.descripcion() != null ? req.descripcion().trim() : "");
        return categoriaRepo.save(actual);
    }

    public void eliminar(Long id) {
        Categoria actual = buscar(id);
        if (!productoRepo.findByCategoriaId(id).isEmpty()) {
            throw new IllegalArgumentException("No se puede eliminar una categoría con productos asociados");
        }
        categoriaRepo.delete(actual);
    }
}