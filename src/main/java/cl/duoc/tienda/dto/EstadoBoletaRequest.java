package cl.duoc.tienda.dto;

import jakarta.validation.constraints.NotBlank;

public record EstadoBoletaRequest(
    @NotBlank String estado
) {}