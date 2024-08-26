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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public void setProyecto(Proyecto proyecto) {
        this.proyecto = proyecto;
    }

    public Miembro getMiembro() {
        return miembro;
    }

    public void setMiembro(Miembro miembro) {
        this.miembro = miembro;
    }
}
