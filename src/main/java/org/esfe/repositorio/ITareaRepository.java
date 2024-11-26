package org.esfe.repositorio;

import org.esfe.modelos.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByEstadoTarea(String estado);
    List<Tarea> findByProyectoProyectoId(Integer proyectoId);
}
