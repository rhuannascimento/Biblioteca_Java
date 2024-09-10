package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.RevistaDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.entity.Revista;
import com.biblioteca.scbapi.service.ObraService;
import com.biblioteca.scbapi.service.RevistaService;
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
@RequestMapping("/api/v1/revista")
@RequiredArgsConstructor
@Api(value = "Revista Controller", tags = {"Revista"})
public class RevistaController {

    private final RevistaService service;
    private final ObraService obraService;

    @GetMapping()
    @ApiOperation(value = "Lista todas as revistas", response = List.class)
    public ResponseEntity get() {
        List<Revista> revistas = service.getRevistas();
        return ResponseEntity.ok(revistas.stream().map(RevistaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém uma revista pelo ID", response = RevistaDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID da revista", required = true) @PathVariable("id") Long id) {
        Optional<Revista> revista = service.getRevistaById(id);
        if (!revista.isPresent()) {
            return new ResponseEntity("Revista não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(revista.map(RevistaDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria uma nova revista", response = RevistaDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados da revista", required = true) @RequestBody RevistaDTO dto) {
        try {
            Revista revista = converter(dto);
            revista = service.salvar(revista);
            return new ResponseEntity(revista, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta uma revista pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID da revista", required = true) @PathVariable("id") Long id) {
        Optional<Revista> revista = service.getRevistaById(id);
        if (!revista.isPresent()) {
            return new ResponseEntity("Revista não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(revista.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza uma revista pelo ID", response = RevistaDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID da revista", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados da revista", required = true) @RequestBody RevistaDTO dto) {
        if (!service.getRevistaById(id).isPresent()) {
            return new ResponseEntity("Revista não encontrada", HttpStatus.NOT_FOUND);
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
