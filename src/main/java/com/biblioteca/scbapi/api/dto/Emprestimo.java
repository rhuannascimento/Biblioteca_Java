package com.biblioteca.scbapi.api.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Emprestimo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private Date dataDeEmprestimo;
    private Date dataDeDevolucao;
    private Date dataPrevistaDeDevolucao;
    private boolean devolvido;
    private float multa;

    @ManyToOne
    private Exemplar exemplar;
    @ManyToOne
    private Tomador tomador;
}