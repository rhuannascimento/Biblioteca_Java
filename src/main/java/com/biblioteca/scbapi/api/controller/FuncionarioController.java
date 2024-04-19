package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.FuncionarioDTO;
import com.biblioteca.scbapi.model.entity.Funcionario;
import com.biblioteca.scbapi.service.FuncionarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {
    private final FuncionarioService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Funcionario> alunos = service.getFuncionarios();
        return ResponseEntity.ok(alunos.stream().map(FuncionarioDTO::create).collect(Collectors.toList()));
    }
}
