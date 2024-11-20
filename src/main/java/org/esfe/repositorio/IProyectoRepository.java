package org.esfe.repositorio;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IProyectoRepository extends JpaRepository<Proyecto, Integer> {
    //Filtrar los proyectos segun el usuario autenticado
    List<Proyecto> findByUsuario(Usuario usuario);
    //Filtrar los proyectos segun el estado
    List<Proyecto> findByEstado(String estado);
    //Lista de proyectos cancelados segun el usuario autenticado
    List<Proyecto> findByUsuarioAndEstado(Usuario usuario, String estado);


    List<Proyecto> findByUsuarioAndEstadoOrderByNombreAsc(Usuario usuario, String estado);
    List<Proyecto> findByUsuarioAndEstadoOrderByFechaFinAsc(Usuario usuario, String estado);
    List<Proyecto> findByUsuarioAndEstadoOrderByPresupuestoDesc(Usuario usuario, String estado);

}