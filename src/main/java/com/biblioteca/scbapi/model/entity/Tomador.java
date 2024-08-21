package com.biblioteca.scbapi.model.entity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tomador extends Pessoa {
    private String complemento;
    private String numero;
    private String logradouro;
    private String cidade;
    private String estado;
    private String pais;
    private int numEmprestimos;
    private boolean emAtarso;
}