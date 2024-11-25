package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Tarea;
import org.esfe.repositorio.ITareaRepository;
import org.esfe.servicios.interfaces.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
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

    public long calcularDiasLaborables(Tarea tarea) {
        // Convertir fechas a objetos LocalDate
        LocalDate inicio = tarea.getFechaInicio().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate fin = tarea.getFechaFin().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Inicializar contador de días laborables
        long diasLaborables = 0;

        // Recorrer cada día entre la fecha de inicio y la fecha de fin
        for (LocalDate dia = inicio; dia.isBefore(fin.plusDays(1)); dia = dia.plusDays(1)) {
            // Verificar si el día es laborable (no es sábado ni domingo)
            if (dia.getDayOfWeek().getValue() < 6) { // 1 = lunes, 2 = martes, ..., 6 = sábado
                diasLaborables++;
            }
        }

        return diasLaborables;
    }

    @Override
    public List<Tarea> obtenerTareasPorProyecto(Integer proyectoId) {
        return TareaRepository.findByProyecto_ProyectoId(proyectoId);

    }
}

