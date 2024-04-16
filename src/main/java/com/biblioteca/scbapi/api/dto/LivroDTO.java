package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Livro;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivroDTO {
    private long id;
    private String autor;
    private long idObra;

    public static LivroDTO create(Livro livro) {
        ModelMapper modelMapper = new ModelMapper();
        LivroDTO dto = modelMapper.map(livro, LivroDTO.class);
        dto.idObra = livro.getObra().getId();
        return dto;
    }
}