package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.LivroDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.service.LivroService;
import com.biblioteca.scbapi.model.entity.Livro;
import com.biblioteca.scbapi.service.ObraService;
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
public class LivroController {
    private final LivroService service;
    private final ObraService obraService;
    @GetMapping()
    public ResponseEntity get() {
        List<Livro> livros= service.getLivros();
        return ResponseEntity.ok(livros.stream().map(LivroDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Livro> livro = service.getLivroById(id);
        if (!livro.isPresent()) {
            return new ResponseEntity("Livro não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(livro.map(LivroDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody LivroDTO dto) {
        try {
            Livro livro = converter(dto);
            livro = service.salvar(livro);
            return new ResponseEntity(livro, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody LivroDTO dto) {
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
            Optional<Obra> obra = obraService.getObraById(dto.(dto.getIdObra()));
            if (!obra.isPresent()) {
                livro.setObra(null);
            } else {
                livro.setObra(obra.get());
            }
        }

        return livro;
    }
}
