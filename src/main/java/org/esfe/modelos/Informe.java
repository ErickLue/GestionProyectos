package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import javax.lang.model.element.Name;
import javax.xml.crypto.Data;

@Entity
@Table(name = "Informe")
public class Informe {

    @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    @NotBlank(message = "El campo es requerido")
   private Integer informeId;
    @NotBlank(message = "El campo es requerido")
   private String nombre;
    @NotBlank(message = "El campo es requerido")
   private Data fechaInicio;
    @NotBlank(message = "El campo es requerido")
   private Data fechaFin;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private Integer estadoId;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Integer  proyectoId;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private Integer usuarioId;

    public Integer getInformeId() {
        return informeId;
    }

    public void setInformeId(Integer informeId) {
        this.informeId = informeId;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Data getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Data fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public Data getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Data fechaFin) {
        this.fechaFin = fechaFin;
    }
}
