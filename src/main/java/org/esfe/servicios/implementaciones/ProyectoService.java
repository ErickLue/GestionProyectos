package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Proyecto;
import org.esfe.repositorio.IProyectoRepository; // Asegúrate de que este repositorio esté definido
import org.esfe.servicios.interfaces.IProyectoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProyectoService implements IProyectoService {

    @Autowired
    private IProyectoRepository proyectoRepository; // Cambiado a IProyectoRepository

    @Override
    public Page<Proyecto> buscarTodosLospaginado(Pageable pageable) {
        return proyectoRepository.findAll(pageable);
    }

    @Override
    public List<Proyecto> ObtenerTodos() {
        return proyectoRepository.findAll();
    }

    @Override
    public Optional<Proyecto> buscarPorId(Integer proyectoId) {
        return proyectoRepository.findById(proyectoId);
    }

    @Override
    public Proyecto crearOEditar(Proyecto proyecto) {
        return proyectoRepository.save(proyecto);
    }

    @Override
    public void eliminarPorid(Integer proyectoId) {
        proyectoRepository.deleteById(proyectoId);
    }
}
