package cl.duoc.tienda.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ProductoRequest(
    @NotBlank @Size(max = 120) String nombre,
    @NotBlank @Size(max = 500) String descripcion,
    @NotNull @Min(0) Integer precio,
    @NotNull @Min(0) Integer stock,
    @Size(max = 255) String imagen,
    @NotNull Long categoriaId
) {}