package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.TomadorDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Tomador;
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
@RequestMapping("/api/v1/tomador")
@RequiredArgsConstructor
@Api(value = "Tomador Controller", tags = {"Tomador"})
public class TomadorController {

    private final TomadorService service;

    @GetMapping()
    @ApiOperation(value = "Lista todos os tomadores", response = List.class)
    public ResponseEntity get() {
        List<Tomador> tomadores = service.getTomadores();
        return ResponseEntity.ok(tomadores.stream().map(TomadorDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um tomador pelo ID", response = TomadorDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do tomador", required = true) @PathVariable("id") Long id) {
        Optional<Tomador> tomador = service.getTomadorById(id);
        if (!tomador.isPresent()) {
            return new ResponseEntity("Tomador não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(tomador.map(TomadorDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo tomador", response = TomadorDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do tomador", required = true) @RequestBody TomadorDTO dto) {
        try {
            Tomador tomador = converter(dto);
            tomador = service.salvar(tomador);
            return new ResponseEntity(tomador, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta um tomador pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do tomador", required = true) @PathVariable("id") Long id) {
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
    @ApiOperation(value = "Atualiza um tomador pelo ID", response = TomadorDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do tomador", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do tomador", required = true) @RequestBody TomadorDTO dto) {
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
