package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Proyecto")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer proyecto_id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La descripcion es requerido")
    private String descripcion;

    @NotNull(message = "El presupuesto es requerido")
    private Double presupuesto;

    @NotBlank(message = "La prioridad es requerido")
    private String prioridad;

    @NotNull(message = "La fecha de inicio es requerido")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @NotNull(message = "La fecha final es requerido")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Estado> estados = new HashSet<>();


    public Integer getProyecto_id() {
        return proyecto_id;
    }

    public void setProyecto_id(Integer proyecto_id) {
        this.proyecto_id = proyecto_id;
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

    public Double getPresupuesto() {
        return presupuesto;
    }

    public void setPresupuesto(Double presupuesto) {
        this.presupuesto = presupuesto;
    }

    public String getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(String prioridad) {
        this.prioridad = prioridad;
    }

    public @NotNull(message = "La fecha de inicio es requerido") Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(@NotNull(message = "La fecha de inicio es requerido") Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public @NotNull(message = "La fecha final es requerido") Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Set<Estado> getEstados() {
        return estados;
    }

    public void setEstados(Set<Estado> estados) {
        this.estados = estados;
    }
}
