package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Obra;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ObraDTO {
    private Long id;
    private String titulo;
    private int numExemplares;
    private float valorMultaPorDiaDeAtraso;
    private String categoria;
    private String editora;

    public static ObraDTO create(Obra obra) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(obra, ObraDTO.class);
    }
}