package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.ReservaDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.entity.Reserva;
import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.service.ObraService;
import com.biblioteca.scbapi.service.ReservaService;
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
@RequestMapping("/api/v1/reserva")
@RequiredArgsConstructor
public class ReservaController {
    private final ReservaService service;
    private final ObraService obraService;
    private final TomadorService tomadorService;
    @GetMapping()
    public ResponseEntity get() {
        List<Reserva> reservas = service.getReservas();
        return ResponseEntity.ok(reservas.stream().map(ReservaDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Reserva> reserva = service.getReservaById(id);
        if (!reserva.isPresent()) {
            return new ResponseEntity("Reserva não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(reserva.map(ReservaDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody ReservaDTO dto) {
        try {
            Reserva reserva = converter(dto);
            reserva = service.salvar(reserva);
            return new ResponseEntity(reserva, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
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
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody ReservaDTO dto) {
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
