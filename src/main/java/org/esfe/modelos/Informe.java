package org.esfe.modelos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "Informes")
public class Informe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer id;

    @NotBlank(message = "El campo es requerido")
    private String nombre;

    @NotBlank(message = "El campo es requerido")
    private Date fechaIncio;

    @NotBlank(message = "El campo es requerido")
    private Date fechaFinal;

    @ManyToOne
    @JoinColumn(name = "estadoId")
    private LocalDate estadoId;

    @ManyToOne
    @JoinColumn(name = "proyectoId")
    private int proyectoId;

    @ManyToOne
    @JoinColumn(name = "usuarioId")
    private int usuarioId;

}
