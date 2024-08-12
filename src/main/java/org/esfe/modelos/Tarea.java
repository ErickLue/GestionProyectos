package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.Date;

@Entity
@Table(name = "Tarea")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer tareaid;
    @NotBlank(message = "El nombre es requerido")
    private String nombre;
    @NotBlank(message = "La descripcion es requerido")
    private String descripcion;
    @NotBlank(message = "El estado es requerido")
    private String estado;
    @NotBlank(message = "La prioridad es requerido")
    private String prioridad;
    @NotBlank(message = "La fecha Inicio es requerido")
    private Date fechaInicio;
    @NotBlank(message = "La fecha Progreso es requerido")
    private Date fechaProgreso;
    @NotBlank(message = "La fecha Fin  es requerido")
    private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private Estado estados;


    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "miembroEquipoId")
    private Miembro miembroEquipo;

    public Integer getTareaid() {
        return tareaid;
    }

    public void setTareaid(Integer tareaid) {
        this.tareaid = tareaid;
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

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }
}
