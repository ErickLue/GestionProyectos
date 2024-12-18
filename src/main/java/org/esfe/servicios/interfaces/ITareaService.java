package org.esfe.servicios.interfaces;

import org.esfe.modelos.Proyecto;
import org.esfe.modelos.Tarea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ITareaService  {
    Page<Tarea> buscarTodosLospaginado(Pageable pageable);

    List<Tarea> ObtenerTodos();

    Optional<Tarea> buscarPorId(Integer tareaId);

    Tarea crearOEditar(Tarea tarea);

    void eliminarPorid(Integer tareaId);

    Map<String, Integer> calcularPorcentajes();

     long calcularDiasLaborables(Tarea tarea);

    List<Tarea> buscarPorEstadoTarea(String estado);

    List<Tarea> obtenerTareasPorProyecto(Integer proyectoId);
}
