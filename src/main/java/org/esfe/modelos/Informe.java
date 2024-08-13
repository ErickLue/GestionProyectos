package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import javax.lang.model.element.Name;
import java.util.Date;

@Entity
@Table(name = "Informe")
public class Informe {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank(message = "El campo es requerido")
   private Integer informe_id;
    @NotBlank(message = "El campo es requerido")
   private String nombre;
    @NotBlank(message = "El campo es requerido")
   private Date fechaInicio;
    @NotBlank(message = "El campo es requerido")
   private Date fechaFin;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private Estado estado;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Proyecto  proyecto;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private Usuario usuario;

    public @NotBlank(message = "El campo es requerido") Integer getInforme_id() {
        return informe_id;
    }

    public void setInforme_id(@NotBlank(message = "El campo es requerido") Integer informe_id) {
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
}