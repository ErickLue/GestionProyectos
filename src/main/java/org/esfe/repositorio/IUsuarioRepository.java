package org.esfe.repositorio;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository <Usuario, Integer>{
    Optional<Usuario> findByCorreoAndContraseña(String correo, String contraseña);
    Optional<Usuario> findByCorreo(String correo);

}
