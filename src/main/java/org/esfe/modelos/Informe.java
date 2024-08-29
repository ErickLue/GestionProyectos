package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import javax.lang.model.element.Name;
import java.util.Date;

@Entity
@Table(name = "Informe")
public class Informe {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Integer informe_id;
    @NotBlank(message = "El campo es requerido")
   private String nombre;
    @NotNull(message = "El campo es requerido")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;
    @NotNull(message = "El campo es requerido")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
   private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "estado_id")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto  proyecto;

    @ManyToOne
    @JoinColumn(name = "miembroEquipo_id")
    private Miembro miembro;

    public Integer getInforme_id() {
        return informe_id;
    }

    public void setInforme_id(Integer informe_id) {
        this.informe_id = informe_id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }
}
