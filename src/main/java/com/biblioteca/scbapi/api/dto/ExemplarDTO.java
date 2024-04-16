package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Exemplar;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExemplarDTO {
    private long id;
    private String prateleira;
    private boolean emprestado;
    private long idObra;

    public static ExemplarDTO create(Exemplar exemplar) {
        ModelMapper modelMapper = new ModelMapper();
        ExemplarDTO dto = modelMapper.map(exemplar, ExemplarDTO.class);
        dto.idObra = exemplar.getObra().getId();
        return dto;
    }
}