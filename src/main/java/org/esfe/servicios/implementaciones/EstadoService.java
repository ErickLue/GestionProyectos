package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Estado;
import org.esfe.repositorio.IEstadoRepository;
import org.esfe.servicios.interfaces.IEstadoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
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
    public Optional<Estado> buscarPorId(Integer estadoId) {
        return estadoRepository.findById(estadoId);
    }

    @Override
    public Estado crearOEditar(Estado estado) {
        return estadoRepository.save(estado);
    }

    @Override
    public void eliminarPorid(Integer estadoId) {
    estadoRepository.deleteById(estadoId);
    }
}
