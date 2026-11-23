package cl.duoc.tienda.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "boletas")
@Getter 
@Setter
public class Boleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false, length = 120)
    private String clienteCorreo;

    @Column(nullable = false, length = 120)
    private String clienteNombre;

    @Column(nullable = false)
    private Integer total;

    @Column(nullable = false, length = 30)
    private String estado;

    @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<DetalleBoleta> detalles = new ArrayList<>();
}