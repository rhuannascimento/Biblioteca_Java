package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.RevistaDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.entity.Revista;
import com.biblioteca.scbapi.service.ObraService;
import com.biblioteca.scbapi.service.RevistaService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@RestController
@RequestMapping("/api/v1/revista")
@RequiredArgsConstructor
public class RevistaController {
    private final RevistaService service;
    private final ObraService obraService;
    @GetMapping()
    public ResponseEntity get() {
        List<Revista> revistas = service.getRevistas();
        return ResponseEntity.ok(revistas.stream().map(RevistaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Revista> revista = service.getRevistaById(id);
        if (!revista.isPresent()) {
            return new ResponseEntity("Revista não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(revista.map(RevistaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody RevistaDTO dto) {
        try {
            Revista revista = converter(dto);
            revista = service.salvar(revista);
            return new ResponseEntity(revista, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Revista> revista = service.getRevistaById(id);
        if (!revista.isPresent()) {
            return new ResponseEntity("Revista não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(revista.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody RevistaDTO dto) {
        if (!service.getRevistaById(id).isPresent()) {
            return new ResponseEntity("Revista não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Revista revista = converter(dto);
            revista.setId(id);
            service.salvar(revista);
            return ResponseEntity.ok(revista);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Revista converter(RevistaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Revista revista = modelMapper.map(dto, Revista.class);

        if (dto.getIdObra() != null) {
            Optional<Obra> obra = obraService.getObraById(dto.getIdObra());
            if (!obra.isPresent()) {
                revista.setObra(null);
            } else {
                revista.setObra(obra.get());
            }
        }

        return revista;
    }
}
