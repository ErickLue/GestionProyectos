package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Estado;
import org.esfe.repositorio.IEstadoRepository;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class EstadoService implements IEstadoServices {
    @Autowired
    private IEstadoRepository estadoRepository;
    @Override
    public Page<Estado> buscarTodosLospaginado(Pageable pageable) {
        return estadoRepository.findAll(pageable);
    }

    @Override
    public List<Estado> ObtenerTodos() {
        return estadoRepository.findAll();
    }

    @Override
    public Optional<Estado> buscarPorId(Integer estadoid) {
        return estadoRepository.findById(estadoid);
    }

    @Override
    public Estado crearOEditar(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Override
    public void eliminarPorid(Integer estadoid) {
    estadoRepository.deleteById(estadoid);
    }
}
