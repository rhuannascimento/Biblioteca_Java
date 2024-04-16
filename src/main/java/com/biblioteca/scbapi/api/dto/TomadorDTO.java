package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Tomador;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomadorDTO {
    private long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String complemento;
    private String numero;
    private String logradouro;
    private String cidade;
    private String estado;
    private String pais;
    private int numEmprestimos;
    private boolean emAtarso;

    public static TomadorDTO create(Tomador tomador) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(tomador, TomadorDTO.class);
    }

}