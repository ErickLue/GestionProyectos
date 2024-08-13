package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Tarea")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tarea_id;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La descripción es requerida")
    private String descripcion;

    @NotBlank(message = "El estadoTarea es requerido")
    private String estadoTarea;

    @NotBlank(message = "La prioridad es requerida")
    private String prioridad;

    @NotNull(message = "La fecha de inicio es requerida")
    private Date fechaInicio;

    @NotNull(message = "La fecha de progreso es requerida")
    private Date fechaProgreso;

    @NotNull(message = "La fecha de fin es requerida")
    private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "miembroEquipoId")
    private Miembro miembroEquipo;

    @ManyToMany
    @JoinTable(
            name = "tarea_estado",
            joinColumns = @JoinColumn(name = "tarea_id"),
            inverseJoinColumns = @JoinColumn(name = "estado_id")
    )
    private Set<Estado> estados = new HashSet<>();

    public Integer getTarea_id() {
        return tarea_id;
    }

    public void setTarea_id(Integer tarea_id) {
        this.tarea_id = tarea_id;
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

    public String getEstadoTarea() {
        return estadoTarea;
    }

    public void setEstadoTarea(String estadoTarea) {
        this.estadoTarea = estadoTarea;
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

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Miembro getMiembroEquipo() {
        return miembroEquipo;
    }

    public void setMiembroEquipo(Miembro miembroEquipo) {
        this.miembroEquipo = miembroEquipo;
    }

    public Set<Estado> getEstados() {
        return estados;
    }

    public void setEstados(Set<Estado> estados) {
        this.estados = estados;
    }
}
