package org.esfe.servicios.interfaces;

import org.esfe.modelos.Usuario;

public interface IUsuarioService {
    Usuario autenticar(String correo, String contraseña);

    Usuario crearOEditar(Usuario usuario);

}
