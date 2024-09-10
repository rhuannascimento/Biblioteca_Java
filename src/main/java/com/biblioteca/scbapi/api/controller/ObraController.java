package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.ObraDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
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
@RequestMapping("/api/v1/obra")
@RequiredArgsConstructor
@Api(value = "Obra Controller", tags = {"Obra"})
public class ObraController {

    private final ObraService service;
    private final ExemplarService exemplarService;

    @GetMapping()
    @ApiOperation(value = "Lista todas as obras", response = List.class)
    public ResponseEntity get() {
        List<Obra> obras = service.getObras();
        return ResponseEntity.ok(obras.stream().map(ObraDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém uma obra pelo ID", response = ObraDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID da obra", required = true) @PathVariable("id") Long id) {
        Optional<Obra> obra = service.getObraById(id);
        if (!obra.isPresent()) {
            return new ResponseEntity("Obra não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(obra.map(ObraDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria uma nova obra", response = ObraDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados da obra", required = true) @RequestBody ObraDTO dto) {
        try {
            Obra obra = converter(dto);
            obra = service.salvar(obra);
            return new ResponseEntity(obra, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta uma obra pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID da obra", required = true) @PathVariable("id") Long id) {
        Optional<Obra> obra = service.getObraById(id);
        if (!obra.isPresent()) {
            return new ResponseEntity("Obra não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            exemplarService.excluirByObraId(obra);
            service.excluir(obra.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza uma obra pelo ID", response = ObraDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID da obra", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados da obra", required = true) @RequestBody ObraDTO dto) {
        if (!service.getObraById(id).isPresent()) {
            return new ResponseEntity("Obra não encontrada", HttpStatus.NOT_FOUND);
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
