package com.biblioteca.scbapi.api.controller;


import com.biblioteca.scbapi.api.dto.TomadorDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.service.TomadorService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/tomador")
@RequiredArgsConstructor
public class TomadorController {
    private final TomadorService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Tomador> tomador = service.getTomadores();
        return ResponseEntity.ok(tomador.stream().map(TomadorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Tomador> tomador = service.getTomadorById(id);
        if (!tomador.isPresent()) {
            return new ResponseEntity("Tomador não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tomador.map(TomadorDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody TomadorDTO dto) {
        try {
            Tomador tomador = converter(dto);
            tomador = service.salvar(tomador);
            return new ResponseEntity(tomador, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Tomador> tomador = service.getTomadorById(id);
        if (!tomador.isPresent()) {
            return new ResponseEntity("Tomador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(tomador.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody TomadorDTO dto) {
        if (!service.getTomadorById(id).isPresent()) {
            return new ResponseEntity("Tomador não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Tomador tomador = converter(dto);
            tomador.setId(id);
            service.salvar(tomador);
            return ResponseEntity.ok(tomador);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Tomador converter(TomadorDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Tomador tomador = modelMapper.map(dto, Tomador.class);

        return tomador;
    }
}
