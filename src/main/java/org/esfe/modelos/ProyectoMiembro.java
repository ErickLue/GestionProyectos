package org.esfe.modelos;

import jakarta.persistence.*;

@Entity
@Table(name = "ProyectoMiembro")
public class ProyectoMiembro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private Proyecto proyecto;

    @ManyToOne
    @JoinColumn(name = "miembroEquipoId")
    private Miembro miembro;

}
