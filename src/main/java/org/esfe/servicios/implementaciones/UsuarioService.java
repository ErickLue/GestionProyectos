package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.esfe.repositorio.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario autenticar(String correo, String contraseña) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreoAndContraseña(correo, contraseña);
        return usuario.orElse(null);
    }

    @Override
    public Usuario crearOEditar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }


}
