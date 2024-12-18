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
    private Integer proyectoId;

    @NotBlank(message = "El nombre es requerido")
    private String nombre;

    @NotBlank(message = "La descripcion es requerida")
    private String descripcion;

    @NotNull(message = "El presupuesto es requerido")
    private Double presupuesto;

    @NotBlank(message = "La prioridad es requerida")
    private String prioridad;

    @NotNull(message = "La fecha de inicio es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaInicio;

    @NotNull(message = "La fecha final es requerida")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date fechaFin;

    @NotNull(message = "El estado es requerido")
    private String estado = "Activo";


    private String imagen;



    //   @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
   // private Set<Estado> estados = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Informe> informes = new HashSet<>();

    @OneToMany(mappedBy = "proyecto", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Tarea> tareas = new HashSet<>(); // Nueva relación con Tarea

    @ManyToOne
    @JoinColumn(name = "usuario_id") // Ajusta según tu esquema
    private Usuario usuario;


    // Getters y Setters

    public Integer getProyectoId() {
        return proyectoId;
    }

    public void setProyectoId(Integer proyectoId) {
        this.proyectoId = proyectoId;
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

    public Set<Informe> getInformes() {
        return informes;
    }

    public void setInformes(Set<Informe> informes) {
        this.informes = informes;
    }

    public Set<Tarea> getTareas() {
        return tareas;
    }

    public void setTareas(Set<Tarea> tareas) {
        this.tareas = tareas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public @NotBlank(message = "La prioridad es requerida") String getEstado() {
        return estado;
    }

    public void setEstado(@NotBlank(message = "La prioridad es requerida") String estado) {
        this.estado = estado;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
