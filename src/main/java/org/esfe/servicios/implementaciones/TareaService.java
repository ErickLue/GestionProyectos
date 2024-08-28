package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Tarea;
import org.esfe.repositorio.ITareaRepository;
import org.esfe.servicios.interfaces.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TareaService implements ITareaService {
    @Autowired
    private ITareaRepository TareaRepository;

    @Override
    public Page<Tarea> buscarTodosLospaginado(Pageable pageable) {
        return TareaRepository.findAll(pageable);
    }

    @Override
    public List<Tarea> ObtenerTodos() {
        return TareaRepository.findAll();
    }

    @Override
    public Optional<Tarea> buscarPorId(Integer tareaId) {
        return TareaRepository.findById(tareaId);
    }

    @Override
    public Tarea crearOEditar(Tarea tarea) {
        return TareaRepository.save(tarea);
    }

    @Override
    public void eliminarPorid(Integer tareaId) {
        TareaRepository.deleteById(tareaId);
    }

    public Map<String, Integer> calcularPorcentajes() {
        List<Tarea> tareas = TareaRepository.findAll();
        long totalTareas = tareas.size();

        if (totalTareas == 0) {
            return Collections.emptyMap();
        }

        return tareas.stream()
                .collect(Collectors.groupingBy(Tarea::getEstadoTarea, Collectors.collectingAndThen(
                        Collectors.counting(),
                        count -> (int) Math.round((double) count / totalTareas * 100)
                )));
    }
}

