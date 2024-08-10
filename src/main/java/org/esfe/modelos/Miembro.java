package org.esfe.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.*;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "MiembroEquipo")
public class Miembro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer miembroEquipoId;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "El cargo es requerido")
    private String cargo;

    public Integer getMiembroEquipoId() {
        return miembroEquipoId;
    }

    public void setMiembroEquipoId(Integer miembroEquipoId) {
        this.miembroEquipoId = miembroEquipoId;
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
