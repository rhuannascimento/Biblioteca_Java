package com.biblioteca.scbapi.model.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Obra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titulo;
    private int numExemplares;
    private float valorMultaPorDiaDeAtraso;
    private String categoria;
    private String editora;

    public void incrementExemplares(){numExemplares++;}
    public void decrementExemplares(){numExemplares--;}

}