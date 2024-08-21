package com.biblioteca.scbapi.model.entity;

import javax.persistence.*;
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
    private Long id;
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