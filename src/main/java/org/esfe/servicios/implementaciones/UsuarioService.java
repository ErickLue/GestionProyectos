package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Usuario;
import org.esfe.repositorio.IProyectoRepository;
import org.esfe.repositorio.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IProyectoRepository proyectoRepository;

    @Override
    public List<Usuario> ObtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario autenticar(String correo, String contraseña) {
        Optional<Usuario> usuario = usuarioRepository.findByCorreoAndContraseña(correo, contraseña);
        return usuario.orElse(null);
    }

    @Override
    public Usuario crearOEditar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> getProyectosByUsuario(Usuario usuario) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

}
