package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.LivroDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Livro;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.service.LivroService;
import com.biblioteca.scbapi.service.ObraService;
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
@RequestMapping("/api/v1/livro")
@RequiredArgsConstructor
@Api(value = "Livro Controller", tags = {"Livro"})
public class LivroController {

    private final LivroService service;
    private final ObraService obraService;

    @GetMapping()
    @ApiOperation(value = "Lista todos os livros", response = List.class)
    public ResponseEntity get() {
        List<Livro> livros = service.getLivros();
        return ResponseEntity.ok(livros.stream().map(LivroDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um livro pelo ID", response = LivroDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do livro", required = true) @PathVariable("id") Long id) {
        Optional<Livro> livro = service.getLivroById(id);
        if (!livro.isPresent()) {
            return new ResponseEntity("Livro não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(livro.map(LivroDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo livro", response = LivroDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do livro", required = true) @RequestBody LivroDTO dto) {
        try {
            Livro livro = converter(dto);
            livro = service.salvar(livro);
            return new ResponseEntity(livro, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um livro pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do livro", required = true) @PathVariable("id") Long id) {
        Optional<Livro> livro = service.getLivroById(id);
        if (!livro.isPresent()) {
            return new ResponseEntity("Livro não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(livro.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza um livro pelo ID", response = LivroDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do livro", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do livro", required = true) @RequestBody LivroDTO dto) {
        if (!service.getLivroById(id).isPresent()) {
            return new ResponseEntity("Livro não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Livro livro = converter(dto);
            livro.setId(id);
            service.salvar(livro);
            return ResponseEntity.ok(livro);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Livro converter(LivroDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Livro livro = modelMapper.map(dto, Livro.class);

        if (dto.getIdObra() != null) {
            Optional<Obra> obra = obraService.getObraById(dto.getIdObra());
            if (!obra.isPresent()) {
                livro.setObra(null);
            } else {
                livro.setObra(obra.get());
            }
        }

        return livro;
    }
}
