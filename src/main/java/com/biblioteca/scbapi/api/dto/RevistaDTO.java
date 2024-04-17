package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Revista;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevistaDTO{

    private Long id;
    private String edicao;
    private int idObra;

    public static RevistaDTO create(Revista revista) {
        ModelMapper modelMapper = new ModelMapper();
        RevistaDTO dto = modelMapper.map(revista, RevistaDTO.class);
        dto.idObra = revista.getObra().getId();

        return dto;
    }
}