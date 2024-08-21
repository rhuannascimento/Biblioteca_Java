package com.biblioteca.scbapi.model.entity;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Funcionario extends Pessoa {
    private String matricula;
    private boolean ativo;
}