package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "tarea")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotBlank(message = "El estado es requerido")
    private String estado;

    @NotBlank(message = "La prioridad es requerida")
    private String prioridad;

    @NotNull(message = "La fecha de inicio es requerida")
    private LocalDate fechaInicio;

    @NotNull(message = "La fecha de progreso es requerida")
    private LocalDate fechaProgreso;

    @NotNull(message = "La fecha final es requerida")
    private LocalDate fechaFinal;

    // Getters y setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaProgreso() {
        return fechaProgreso;
    }

    public void setFechaProgreso(Date fechaProgreso) {
        this.fechaProgreso = fechaProgreso;
    }

    public Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }
}
