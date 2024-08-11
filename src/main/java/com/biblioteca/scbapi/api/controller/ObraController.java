package com.biblioteca.scbapi.api.controller;


import com.biblioteca.scbapi.api.dto.ObraDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
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
@RequestMapping("/api/v1/obra")
@RequiredArgsConstructor
public class ObraController {
    private final ObraService service;

    @GetMapping()
    public ResponseEntity get() {
        List<Obra> obras = service.getObras();
        return ResponseEntity.ok(obras.stream().map(ObraDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Obra> obra = service.getObraById(id);
        if (!obra.isPresent()) {
            return new ResponseEntity("Obra não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(obra.map(ObraDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ObraDTO dto) {
        try {
            Obra obra = converter(dto);
            obra = service.salvar(obra);
            return new ResponseEntity(obra, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Obra> obra = service.getObraById(id);
        if (!obra.isPresent()) {
            return new ResponseEntity("Obra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(obra.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ObraDTO dto) {
        if (!service.getObraById(id).isPresent()) {
            return new ResponseEntity("Obra não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Obra obra = converter(dto);
            obra.setId(id);
            service.salvar(obra);
            return ResponseEntity.ok(obra);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Obra converter(ObraDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Obra obra = modelMapper.map(dto, Obra.class);

        return obra;
    }
}
