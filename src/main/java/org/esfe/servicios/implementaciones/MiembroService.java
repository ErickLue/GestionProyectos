package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Miembro;
import org.esfe.repositorio.IMiembroRepository;
import org.esfe.servicios.interfaces.IMiembroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class MiembroService implements IMiembroService {

    @Autowired
    private IMiembroRepository miembroRepository;

    @Override
    public Page<Miembro> buscarTodosPaginados(Pageable pageable) {
        return miembroRepository.findAll(pageable);
    }

    @Override
    public List<Miembro> obtenerTodos() {
        return miembroRepository.findAll();
    }

    @Override
    public Optional<Miembro> buscarPorId(Integer id) {
        return miembroRepository.findById(id);
    }

    @Override
    public Miembro createOEditar(Miembro miembro) {
        return miembroRepository.save(miembro);
    }

    @Override
    public void eliminarPorId(Integer id) {
        miembroRepository.deleteById(id);
    }
}
