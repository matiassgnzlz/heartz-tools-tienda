package cl.duoc.tienda.dto;

public record DetalleBoletaResponse(
    Long id,
    Long productoId,
    String productoNombre,
    Integer cantidad,
    Integer precioUnitario,
    Integer subtotal
) {}