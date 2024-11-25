package org.esfe.repositorio;

import org.esfe.modelos.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITareaRepository extends JpaRepository<Tarea, Integer> {
    List<Tarea> findByProyecto_ProyectoId(Integer proyectoId);

}
