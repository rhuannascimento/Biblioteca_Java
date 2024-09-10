package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.ReservaDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.entity.Reserva;
import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.service.ObraService;
import com.biblioteca.scbapi.service.ReservaService;
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
@RequestMapping("/api/v1/reserva")
@RequiredArgsConstructor
@Api(value = "Reserva Controller", tags = {"Reserva"})
public class ReservaController {

    private final ReservaService service;
    private final ObraService obraService;
    private final TomadorService tomadorService;

    @GetMapping()
    @ApiOperation(value = "Lista todas as reservas", response = List.class)
    public ResponseEntity get() {
        List<Reserva> reservas = service.getReservas();
        return ResponseEntity.ok(reservas.stream().map(ReservaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém uma reserva pelo ID", response = ReservaDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID da reserva", required = true) @PathVariable("id") Long id) {
        Optional<Reserva> reserva = service.getReservaById(id);
        if (!reserva.isPresent()) {
            return new ResponseEntity("Reserva não encontrada", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(reserva.map(ReservaDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria uma nova reserva", response = ReservaDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados da reserva", required = true) @RequestBody ReservaDTO dto) {
        try {
            Reserva reserva = converter(dto);
            reserva = service.salvar(reserva);
            return new ResponseEntity(reserva, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    @ApiOperation(value = "Deleta uma reserva pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID da reserva", required = true) @PathVariable("id") Long id) {
        Optional<Reserva> reserva = service.getReservaById(id);
        if (!reserva.isPresent()) {
            return new ResponseEntity("Reserva não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(reserva.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza uma reserva pelo ID", response = ReservaDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID da reserva", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados da reserva", required = true) @RequestBody ReservaDTO dto) {
        if (!service.getReservaById(id).isPresent()) {
            return new ResponseEntity("Reserva não encontrada", HttpStatus.NOT_FOUND);
        }
        try {
            Reserva reserva = converter(dto);
            reserva.setId(id);
            service.salvar(reserva);
            return ResponseEntity.ok(reserva);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Reserva converter(ReservaDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Reserva reserva = modelMapper.map(dto, Reserva.class);

        if (dto.getIdObra() != null) {
            Optional<Obra> obra = obraService.getObraById(dto.getIdObra());
            if (!obra.isPresent()) {
                reserva.setObra(null);
            } else {
                reserva.setObra(obra.get());
            }
        }
        if (dto.getIdTomador() != null) {
            Optional<Tomador> tomador = tomadorService.getTomadorById(dto.getIdTomador());
            if (!tomador.isPresent()) {
                reserva.setTomador(null);
            } else {
                reserva.setTomador(tomador.get());
            }
        }

        return reserva;
    }
}
