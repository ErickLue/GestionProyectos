package org.esfe.repositorio;


import org.esfe.modelos.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository <Usuario, Integer>{
    Optional<Usuario> findByCorreo(String correo); // Busca por correo
}
