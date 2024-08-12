package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Tarea;
import org.esfe.servicios.interfaces.ITareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TareaService implements ITareaService {
    @Autowired
    private ITareaService tareaService;
    public Page<Tarea> buscarTodosLospaginado(Pageable pageable) {
        return null;
    }

    @Override
    public List<Tarea> ObtenerTodos() {
        return List.of();
    }

    @Override
    public Optional<Tarea> buscarPorId(Integer tareaid) {
        return Optional.empty();
    }

    @Override
    public Tarea crearOEditar(Tarea tarea) {
        return null;
    }

    @Override
    public void eliminarPorid(Integer tareaid) {

    }



}
