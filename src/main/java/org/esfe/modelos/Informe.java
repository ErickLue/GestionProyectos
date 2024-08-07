package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Informes")
public class Informe {
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public @NotBlank(message = "El campo es requerido") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El campo es requerido") String nombre) {
        this.nombre = nombre;
    }

    public @NotBlank(message = "El campo es requerido") Date getFechaIncio() {
        return fechaIncio;
    }

    public void setFechaIncio(@NotBlank(message = "El campo es requerido") Date fechaIncio) {
        this.fechaIncio = fechaIncio;
    }

    public @NotBlank(message = "El campo es requerido") Date getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(@NotBlank(message = "El campo es requerido") Date fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public LocalDate getEstadoId() {
        return estadoId;
    }

    public void setEstadoId(LocalDate estadoId) {
        this.estadoId = estadoId;
    }

    public int getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(int proyectoId) {
        this.proyectoId = proyectoId;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @NotBlank(message = "El campo es requerido")
    private String nombre;

    @NotBlank(message = "El campo es requerido")
    private Date fechaIncio;

    @NotBlank(message = "El campo es requerido")
    private Date fechaFinal;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private LocalDate estadoId;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private int proyectoId;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private int usuarioId;

}
