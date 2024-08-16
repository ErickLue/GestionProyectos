package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Informe;
import org.esfe.modelos.Miembro;
import org.esfe.repositorio.IInformeRepository;
import org.esfe.servicios.interfaces.IInformeServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class InformeService implements IInformeServices {
    @Autowired
    private IInformeRepository informeRepository;

    @Override
    public Page<Informe> buscartodoslospaginados(Pageable Pageable) {
        return informeRepository.findAll(Pageable);
    }

    @Override
    public List<Informe> ObtenerTodos() {
        return informeRepository.findAll();
    }

    @Override
    public Optional<Informe> buscarPorId(Integer informeId) {
        return informeRepository.findById(informeId);
    }
    @Override
    public Informe crearOEditar(Informe informe) {
        return informeRepository.save(informe);
    }

    @Override
    public void eliminarPorid(Integer informeId) {
        informeRepository.deleteById(informeId);
    }
}