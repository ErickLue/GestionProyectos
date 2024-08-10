package org.esfe.repositorio;

import org.esfe.modelos.Miembro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IMiembroRepository extends JpaRepository<Miembro, Integer> {
}
