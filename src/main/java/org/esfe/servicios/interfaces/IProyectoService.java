package org.esfe.servicios.interfaces;

import org.esfe.modelos.Proyecto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IProyectoService {
    Page<Proyecto> buscarTodosLospaginado(Pageable pageable);

    List<Proyecto> ObtenerTodos();

    Optional<Proyecto> buscarPorId(Integer proyectoId);

    Proyecto crearOEditar(Proyecto proyecto);

    void eliminarPorid(Integer proyectoId);
}
