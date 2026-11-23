package cl.duoc.tienda.controller;

import cl.duoc.tienda.dto.BoletaRequest;
import cl.duoc.tienda.dto.BoletaResponse;
import cl.duoc.tienda.dto.EstadoBoletaRequest;
import cl.duoc.tienda.service.BoletaService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boletas")
public class BoletaController {

    private final BoletaService service;

    public BoletaController(BoletaService service) {
        this.service = service;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BoletaResponse crear(@Valid @RequestBody BoletaRequest req, Authentication auth) {
        String correo = auth.getName();
        String nombre = auth.getDetails() != null ? auth.getDetails().toString() : correo;
        return service.crear(req, correo, nombre);
    }

    @GetMapping("/mis-boletas")
    public List<BoletaResponse> misBoletas(Authentication auth) {
        return service.listarPorCliente(auth.getName());
    }

    @GetMapping
    public List<BoletaResponse> listarTodas() {
        return service.listarTodas();
    }

    @GetMapping("/{id}")
    public BoletaResponse buscar(@PathVariable Long id) {
        return service.buscar(id);
    }

    @PutMapping("/{id}/estado")
    public BoletaResponse actualizarEstado(@PathVariable Long id,
                                           @Valid @RequestBody EstadoBoletaRequest req) {
        return service.actualizarEstado(id, req.estado());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        service.eliminar(id);
    }
}