package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.EmprestimoDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Emprestimo;
import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.service.EmprestimoService;
import com.biblioteca.scbapi.service.ExemplarService;
import com.biblioteca.scbapi.service.TomadorService;
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
@RequestMapping("/api/v1/emprestimo")
@RequiredArgsConstructor
@Api(value = "Emprestimo Controller", tags = {"Emprestimo"})
public class EmprestimoController {

    private final EmprestimoService service;
    private final TomadorService tomadorService;
    private final ExemplarService exemplarService;

    @GetMapping()
    @ApiOperation(value = "Lista todos os empréstimos", response = List.class)
    public ResponseEntity get() {
        List<Emprestimo> emprestimos = service.getEmprestimos();
        return ResponseEntity.ok(emprestimos.stream().map(EmprestimoDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um empréstimo pelo ID", response = EmprestimoDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do empréstimo", required = true) @PathVariable("id") Long id) {
        Optional<Emprestimo> emprestimo = service.getEmprestimoById(id);
        if (!emprestimo.isPresent()) {
            return new ResponseEntity("Emprestimo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(emprestimo.map(EmprestimoDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo empréstimo", response = EmprestimoDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do empréstimo", required = true) @RequestBody EmprestimoDTO dto) {
        try {
            Emprestimo emprestimo = converter(dto);
            emprestimo = service.salvar(emprestimo);
            return new ResponseEntity(emprestimo, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um empréstimo pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do empréstimo", required = true) @PathVariable("id") Long id) {
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
    @ApiOperation(value = "Atualiza um empréstimo pelo ID", response = EmprestimoDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do empréstimo", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do empréstimo", required = true) @RequestBody EmprestimoDTO dto) {
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

        if (dto.getIdTomador() != null) {
            Optional<Tomador> tomador = tomadorService.getTomadorById(dto.getIdTomador());
            if (!tomador.isPresent()) {
                emprestimo.setTomador(null);
            } else {
                emprestimo.setTomador(tomador.get());
            }
        }

        if (dto.getIdExemplar() != null) {
            Optional<Exemplar> exemplar = exemplarService.getExemplarById(dto.getIdExemplar());
            if (!exemplar.isPresent()) {
                emprestimo.setExemplar(null);
            } else {
                emprestimo.setExemplar(exemplar.get());
            }
        }

        return emprestimo;
    }
}
