package org.esfe.modelos;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "ProyectoMiembro")
public class ProyectoMiembro {

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Integer  proyectoId;

    @ManyToOne
    @JoinColumn(name = "miembroEquipoId")
    private Integer  miembroEquipoId;

}
