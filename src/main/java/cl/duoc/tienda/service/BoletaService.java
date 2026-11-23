package cl.duoc.tienda.service;

import cl.duoc.tienda.dto.BoletaRequest;
import cl.duoc.tienda.dto.BoletaResponse;
import cl.duoc.tienda.dto.DetalleBoletaResponse;
import cl.duoc.tienda.dto.ItemBoletaRequest;
import cl.duoc.tienda.exception.RecursoNoEncontradoException;
import cl.duoc.tienda.model.Boleta;
import cl.duoc.tienda.model.DetalleBoleta;
import cl.duoc.tienda.model.Producto;
import cl.duoc.tienda.repository.BoletaRepository;
import cl.duoc.tienda.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoletaService {

    private static final Set<String> ESTADOS_VALIDOS = Set.of(
        "PAGADA", "EN_PREPARACION", "ENTREGADA", "ANULADA"
    );

    private final BoletaRepository boletaRepo;
    private final ProductoRepository productoRepo;

    public BoletaService(BoletaRepository boletaRepo, ProductoRepository productoRepo) {
        this.boletaRepo = boletaRepo;
        this.productoRepo = productoRepo;
    }

    @Transactional
    public BoletaResponse crear(BoletaRequest req, String clienteCorreo, String clienteNombre) {
        Boleta boleta = new Boleta();
        boleta.setFecha(LocalDateTime.now());
        boleta.setClienteCorreo(clienteCorreo);
        boleta.setClienteNombre(clienteNombre);
        boleta.setEstado("PAGADA");

        int total = 0;

        for (ItemBoletaRequest item : req.items()) {
            Producto producto = productoRepo.findById(item.productoId())
                .orElseThrow(() -> new RecursoNoEncontradoException(
                    "Producto no encontrado: " + item.productoId()));

            if (producto.getStock() < item.cantidad()) {
                throw new IllegalArgumentException(
                    "Stock insuficiente para " + producto.getNombre()
                        + " (disponible: " + producto.getStock() + ")");
            }

            producto.setStock(producto.getStock() - item.cantidad());
            productoRepo.save(producto);

            int subtotal = producto.getPrecio() * item.cantidad();
            total += subtotal;

            DetalleBoleta detalle = new DetalleBoleta();
            detalle.setBoleta(boleta);
            detalle.setProducto(producto);
            detalle.setCantidad(item.cantidad());
            detalle.setPrecioUnitario(producto.getPrecio());
            detalle.setSubtotal(subtotal);
            boleta.getDetalles().add(detalle);
        }

        boleta.setTotal(total);
        Boleta guardada = boletaRepo.save(boleta);
        return toResponse(guardada);
    }

    public List<BoletaResponse> listarTodas() {
        return boletaRepo.findAllByOrderByFechaDesc().stream()
            .map(this::toResponse)
            .toList();
    }

    public List<BoletaResponse> listarPorCliente(String correo) {
        return boletaRepo.findByClienteCorreoIgnoreCaseOrderByFechaDesc(correo).stream()
            .map(this::toResponse)
            .toList();
    }

    public BoletaResponse buscar(Long id) {
        Boleta boleta = boletaRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Boleta no encontrada: " + id));
        return toResponse(boleta);
    }

    public BoletaResponse actualizarEstado(Long id, String estado) {
        String normalizado = estado.trim().toUpperCase();
        if (!ESTADOS_VALIDOS.contains(normalizado)) {
            throw new IllegalArgumentException("Estado inválido. Permitidos: " + ESTADOS_VALIDOS);
        }
        Boleta boleta = boletaRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Boleta no encontrada: " + id));
        boleta.setEstado(normalizado);
        return toResponse(boletaRepo.save(boleta));
    }

    public void eliminar(Long id) {
        Boleta boleta = boletaRepo.findById(id)
            .orElseThrow(() -> new RecursoNoEncontradoException("Boleta no encontrada: " + id));
        boletaRepo.delete(boleta);
    }

    private BoletaResponse toResponse(Boleta boleta) {
        List<DetalleBoletaResponse> detalles = boleta.getDetalles().stream()
            .map(d -> new DetalleBoletaResponse(
                d.getId(),
                d.getProducto().getId(),
                d.getProducto().getNombre(),
                d.getCantidad(),
                d.getPrecioUnitario(),
                d.getSubtotal()
            ))
            .toList();

        return new BoletaResponse(
            boleta.getId(),
            boleta.getFecha(),
            boleta.getClienteCorreo(),
            boleta.getClienteNombre(),
            boleta.getTotal(),
            boleta.getEstado(),
            detalles
        );
    }
}