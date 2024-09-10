package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.ExemplarDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.service.ExemplarService;
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
@RequestMapping("/api/v1/exemplar")
@RequiredArgsConstructor
@Api(value = "Exemplar Controller", tags = {"Exemplar"})
public class ExemplarController {

    private final ExemplarService service;
    private final ObraService obraService;

    @GetMapping()
    @ApiOperation(value = "Lista todos os exemplares", response = List.class)
    public ResponseEntity get() {
        List<Exemplar> exemplares = service.getExemplares();
        return ResponseEntity.ok(exemplares.stream().map(ExemplarDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um exemplar pelo ID", response = ExemplarDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do exemplar", required = true) @PathVariable("id") Long id) {
        Optional<Exemplar> exemplar = service.getExemplarById(id);
        if (!exemplar.isPresent()) {
            return new ResponseEntity("Exemplar não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(exemplar.map(ExemplarDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo exemplar", response = ExemplarDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do exemplar", required = true) @RequestBody ExemplarDTO dto) {
        try {
            Exemplar exemplar = converter(dto);
            exemplar = service.salvar(exemplar);
            obraService.incrementExemplar(exemplar.getObra());
            return new ResponseEntity(exemplar, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um exemplar pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do exemplar", required = true) @PathVariable("id") Long id) {
        Optional<Exemplar> exemplar = service.getExemplarById(id);
        if (!exemplar.isPresent()) {
            return new ResponseEntity("Exemplar não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(exemplar.get());
            obraService.decrementExemplar(exemplar.get().getObra());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza um exemplar pelo ID", response = ExemplarDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do exemplar", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do exemplar", required = true) @RequestBody ExemplarDTO dto) {
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
