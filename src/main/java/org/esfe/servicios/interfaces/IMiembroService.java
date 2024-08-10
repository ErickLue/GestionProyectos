package org.esfe.servicios.interfaces;

import org.esfe.modelos.Miembro;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IMiembroService {
    Page<Miembro> buscarTodosPaginados(Pageable pageable);

    List<Miembro> obtenerTodos();

    Optional<Miembro> buscarPorId(Integer id);

    Miembro createOEditar(Miembro miembro);

    void eliminarPorId(Integer id);

}
