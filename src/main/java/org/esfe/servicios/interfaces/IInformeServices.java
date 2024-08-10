package org.esfe.servicios.interfaces;

import org.esfe.modelos.Estado;
import org.esfe.modelos.Informe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IInformeServices {

    Page<Informe> buscartodoslospaginados(Pageable Pageable);

    List<Informe> ObtenerTodos();

    Optional<Informe> buscarPorId(Integer informeId);

    Informe crearOEditar(Informe informe);

    void eliminarPorid(Integer informeId);
}
