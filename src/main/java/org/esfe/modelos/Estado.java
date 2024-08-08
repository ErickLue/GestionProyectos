package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Estado")
public class Estado {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer estadoid;
    @NotBlank(message = "El estado es requerido")
    private String estado;

    public Integer getEstadoid() {
        return estadoid;
    }

    public void setEstadoid(Integer estadoid) {
        this.estadoid = estadoid;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
