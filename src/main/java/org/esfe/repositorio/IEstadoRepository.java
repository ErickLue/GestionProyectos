package org.esfe.repositorio;

import org.esfe.modelos.Estado;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IEstadoRepository extends JpaRepository<Estado, Integer> {
}
