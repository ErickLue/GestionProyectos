package org.esfe.repositorio;

import org.esfe.modelos.Tarea;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ITareaRepository extends JpaRepository<Tarea, Integer> {

}
