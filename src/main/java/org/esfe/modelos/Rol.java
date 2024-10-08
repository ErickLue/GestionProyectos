package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.apache.logging.log4j.message.Message;

@Entity
@Table(name = "Rol")
public class Rol {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Integer rol_id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private Estado estado;

    public Integer getRol_id() {
        return rol_id;
    }

    public void setRol_id(Integer rol_id) {
        this.rol_id = rol_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }
}
