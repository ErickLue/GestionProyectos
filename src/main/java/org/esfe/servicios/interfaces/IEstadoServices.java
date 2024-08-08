package org.esfe.servicios.interfaces;

import org.esfe.modelos.Estado;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IEstadoServices {


    Page<Estado> buscarTodosLospaginado(Pageable pageable);

    List<Estado> ObtenerTodos();

    Optional<Estado> buscarPorId(Integer estadoid);

    Estado crearOEditar(Estado estado);

    void eliminarPorid(Integer estadoid);
}
