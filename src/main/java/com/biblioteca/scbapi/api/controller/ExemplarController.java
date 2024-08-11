package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.ExemplarDTO;

import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.service.ExemplarService;
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
@RequestMapping("/api/v1/exemplar")
@RequiredArgsConstructor
public class ExemplarController {
    private final ExemplarService service;
    private final ObraService obraService;
    @GetMapping()
    public ResponseEntity get() {
        List<Exemplar> exemplares = service.getExemplares();
        return ResponseEntity.ok(exemplares.stream().map(ExemplarDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Exemplar> exemplar = service.getExemplarById(id);
        if (!exemplar.isPresent()) {
            return new ResponseEntity("Exemplar não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(exemplar.map(ExemplarDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ExemplarDTO dto) {
        try {
            Exemplar exemplar = converter(dto);
            exemplar = service.salvar(exemplar);
            return new ResponseEntity(exemplar, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Exemplar> exemplar = service.getExemplarById(id);
        if (!exemplar.isPresent()) {
            return new ResponseEntity("Exemplar não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(exemplar.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ExemplarDTO dto) {
        if (!service.getExemplarById(id).isPresent()) {
            return new ResponseEntity("Exemplar não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Exemplar exemplar = converter(dto);
            exemplar.setId(id);
            service.salvar(exemplar);
            return ResponseEntity.ok(exemplar);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Exemplar converter(ExemplarDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Exemplar exemplar = modelMapper.map(dto, Exemplar.class);

        if (dto.getIdObra() != null) {
            Optional<Obra> obra = obraService.getObraById(dto.getIdObra());
            if (!obra.isPresent()) {
                exemplar.setObra(null);
            } else {
                exemplar.setObra(obra.get());
            }
        }

        return exemplar;
    }


}
