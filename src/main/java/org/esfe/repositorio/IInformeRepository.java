package org.esfe.repositorio;

import org.esfe.modelos.Informe;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IInformeRepository extends JpaRepository<Informe, Integer> {
}
