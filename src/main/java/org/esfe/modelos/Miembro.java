package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "MiembroEquipo")
public class Miembro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer miembroEquipo_id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El cargo es requerido")
    private String cargo;

    public Integer getMiembroEquipo_id() {
        return miembroEquipo_id;
    }

    public void setMiembroEquipo_id(Integer miembroEquipo_id) {
        this.miembroEquipo_id = miembroEquipo_id;
    }

    public @NotBlank(message = "El nombre es requerido") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es requerido") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El cargo es requerido") String getCargo() {
        return cargo;
    }

    public void setCargo(@NotBlank(message = "El cargo es requerido") String cargo) {
        this.cargo = cargo;
    }
}
