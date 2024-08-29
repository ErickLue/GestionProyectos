package org.esfe.repositorio;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProyectoRepository extends JpaRepository<Proyecto, Integer> {
    List<Proyecto> findByUsuario(Usuario usuario);
}
