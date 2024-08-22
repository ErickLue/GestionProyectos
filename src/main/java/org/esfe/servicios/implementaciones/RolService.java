package org.esfe.servicios.implementaciones;

import org.esfe.modelos.Rol;
import org.esfe.repositorio.IRolRepository;
import org.esfe.servicios.interfaces.IRolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RolService implements IRolService {
    @Autowired
    private IRolRepository RolRepository;

    @Override
    public List<Rol> ObtenerTodos() {
        return RolRepository.findAll();
    }
}
