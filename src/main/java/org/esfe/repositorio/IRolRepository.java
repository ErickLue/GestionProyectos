package org.esfe.repositorio;

import org.esfe.modelos.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IRolRepository extends JpaRepository <Rol, Integer>{
    Page<Rol> findByOrderByTeacherDesc(Pageable pageable);
}
