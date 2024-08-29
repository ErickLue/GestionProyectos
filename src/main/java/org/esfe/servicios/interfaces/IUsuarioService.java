package org.esfe.servicios.interfaces;

import org.esfe.modelos.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> ObtenerTodos();

    Usuario autenticar(String correo, String contraseña);

    Usuario crearOEditar(Usuario usuario);

    Optional<Usuario> getProyectosByUsuario(Usuario usuario);

    Optional<Usuario> findByCorreo(String correo);



}
