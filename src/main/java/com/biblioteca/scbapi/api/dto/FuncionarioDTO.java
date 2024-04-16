package com.biblioteca.scbapi.api.dto;

import com.biblioteca.scbapi.model.entity.Funcionario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FuncionarioDTO{
    private long id;
    private String nome;
    private String cpf;
    private String email;
    private String telefone;
    private String matricula;
    private boolean ativo;

    public static FuncionarioDTO create(Funcionario funcionario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(funcionario, Funcionario.class);
    }
}