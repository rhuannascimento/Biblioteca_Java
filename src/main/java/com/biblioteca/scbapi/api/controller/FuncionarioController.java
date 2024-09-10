package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.FuncionarioDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Funcionario;
import com.biblioteca.scbapi.service.FuncionarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionario")
@RequiredArgsConstructor
@Api(value = "Funcionario Controller", tags = {"Funcionario"})
public class FuncionarioController {

    private final FuncionarioService service;

    @GetMapping()
    @ApiOperation(value = "Lista todos os funcionários", response = List.class)
    public ResponseEntity get() {
        List<Funcionario> funcionarios = service.getFuncionarios();
        return ResponseEntity.ok(funcionarios.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um funcionário pelo ID", response = FuncionarioDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do funcionário", required = true) @PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(funcionario.map(FuncionarioDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo funcionário", response = FuncionarioDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do funcionário", required = true) @RequestBody FuncionarioDTO dto) {
        try {
            Funcionario funcionario = converter(dto);
            funcionario = service.salvar(funcionario);
            return new ResponseEntity(funcionario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um funcionário pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do funcionário", required = true) @PathVariable("id") Long id) {
        Optional<Funcionario> funcionario = service.getFuncionarioById(id);
        if (!funcionario.isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(funcionario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza um funcionário pelo ID", response = FuncionarioDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do funcionário", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do funcionário", required = true) @RequestBody FuncionarioDTO dto) {
        if (!service.getFuncionarioById(id).isPresent()) {
            return new ResponseEntity("Funcionário não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Funcionario funcionario = converter(dto);
            funcionario.setId(id);
            service.salvar(funcionario);
            return ResponseEntity.ok(funcionario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Funcionario converter(FuncionarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(dto, Funcionario.class);
    }
}
