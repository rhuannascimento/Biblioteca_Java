package com.biblioteca.scbapi.api.controller;


import com.biblioteca.scbapi.api.dto.EmprestimoDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Emprestimo;
import com.biblioteca.scbapi.service.EmprestimoService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/emprestimo")
@RequiredArgsConstructor
public class EmprestimoController {
    private final EmprestimoService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Emprestimo> emprestimos = service.getEmprestimos();
        return ResponseEntity.ok(emprestimos.stream().map(EmprestimoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Emprestimo> emprestimo = service.getEmprestimoById(id);
        if (!emprestimo.isPresent()) {
            return new ResponseEntity("Emprestimo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(emprestimo.map(EmprestimoDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody EmprestimoDTO dto) {
        try {
            Emprestimo emprestimo = converter(dto);
            emprestimo = service.salvar(emprestimo);
            return new ResponseEntity(emprestimo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Emprestimo> emprestimo = service.getEmprestimoById(id);
        if (!emprestimo.isPresent()) {
            return new ResponseEntity("Emprestimo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(emprestimo.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody EmprestimoDTO dto) {
        if (!service.getEmprestimoById(id).isPresent()) {
            return new ResponseEntity("Emprestimo não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Emprestimo emprestimo = converter(dto);
            emprestimo.setId(id);
            service.salvar(emprestimo);
            return ResponseEntity.ok(emprestimo);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Emprestimo converter(EmprestimoDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Emprestimo emprestimo = modelMapper.map(dto, Emprestimo.class);

        return emprestimo;
    }


}
