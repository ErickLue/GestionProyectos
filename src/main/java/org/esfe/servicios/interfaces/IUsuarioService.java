package org.esfe.servicios.interfaces;

import org.esfe.modelos.Informe;
import org.esfe.modelos.Usuario;

import java.util.List;

public interface IUsuarioService {

    List<Usuario> ObtenerTodos();

    Usuario autenticar(String correo, String contraseña);

    Usuario crearOEditar(Usuario usuario);

}
