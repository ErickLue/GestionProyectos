package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;


import java.util.List;

@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer usuario_id;

    private int status;

    @NotBlank (message = "El correo electronico es necesario")
    private String correo;

    @NotBlank (message = "El nombre es necesario")
    private String nombre;

    @NotBlank (message = "La contraseña es necesaria")
    private String contraseña;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usuario_rol",
            joinColumns = @JoinColumn(name = "usuario_id"),
            inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private List<Rol> Rol;

    public @NotBlank(message = "El nombre es necesario") String getNombre() {
        return nombre;
    }

    public void setNombre(@NotBlank(message = "El nombre es necesario") String nombre) {
        this.nombre = nombre;
    }

    public List<org.esfe.modelos.Rol> getRol() {
        return Rol;
    }

    public void setRol(List<org.esfe.modelos.Rol> rol) {
        Rol = rol;
    }

    public Integer getUsuario_id() {
        return usuario_id;
    }

    public void setUsuario_id(Integer usuario_id) {
        this.usuario_id = usuario_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

}
