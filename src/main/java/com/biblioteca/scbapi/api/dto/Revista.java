package com.biblioteca.scbapi.api.dto;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Revista{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String edicao;
    @ManyToOne
    private Obra obra;
}