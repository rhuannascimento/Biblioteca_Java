package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Emprestimo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmprestimoDTO {
    private Long id;
    private Date dataDeEmprestimo;
    private Date dataDeDevolucao;
    private Date dataPrevistaDeDevolucao;
    private boolean devolvido;
    private float multa;
    private Long idExemplar;
    private Long idTomador;

    public static EmprestimoDTO create(Emprestimo emprestimo) {
        ModelMapper modelMapper = new ModelMapper();
        EmprestimoDTO dto = modelMapper.map(emprestimo, EmprestimoDTO.class);
        dto.idExemplar = emprestimo.getExemplar().getId();
        dto.idTomador = emprestimo.getTomador().getId();
        return dto;
    }
}