package org.esfe.servicios.implementaciones;

import jakarta.transaction.Transactional;
import org.esfe.modelos.Usuario;
import org.esfe.repositorio.IProyectoRepository;
import org.esfe.repositorio.IUsuarioRepository;
import org.esfe.servicios.interfaces.IUsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Autowired
    private IProyectoRepository proyectoRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public List<Usuario> ObtenerTodos() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario autenticar(String correo, String contraseña) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findByCorreo(correo);

        if (usuarioOptional.isPresent()) {
            Usuario usuario = usuarioOptional.get();

            // Compara la contraseña ingresada con la contraseña encriptada
            if (passwordEncoder.matches(contraseña, usuario.getContraseña())) {
                return usuario; // Devuelve el usuario si las contraseñas coinciden
            }
        }
        return null; // Retorna null si no se encuentra el usuario o la contraseña no coincide
    }

    @Override
    public Usuario crearOEditar(Usuario usuario) {
        if (usuario.getContraseña() != null && !usuario.getContraseña().isEmpty()) {
            // Encripta la contraseña antes de guardar
            String passwordEncriptada = passwordEncoder.encode(usuario.getContraseña());
            usuario.setContraseña(passwordEncriptada);
        }
        return usuarioRepository.save(usuario);
    }

    @Override
    public Optional<Usuario> getProyectosByUsuario(Usuario usuario) {
        return Optional.empty(); // Puedes implementar esta lógica si es necesario
    }

    @Override
    public Optional<Usuario> findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
