package cl.duoc.tienda.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BoletaResponse(
    Long id,
    LocalDateTime fecha,
    String clienteCorreo,
    String clienteNombre,
    Integer total,
    String estado,
    List<DetalleBoletaResponse> detalles
) {}