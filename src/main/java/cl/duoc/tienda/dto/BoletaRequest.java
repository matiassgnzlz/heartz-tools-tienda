package cl.duoc.tienda.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record BoletaRequest(
    @NotEmpty @Valid List<ItemBoletaRequest> items
) {}