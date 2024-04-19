package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Reserva;
import com.biblioteca.scbapi.model.repository.ReservaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ReservaService {
    private ReservaRepository repository;

    public ReservaService(ReservaRepository repository) {
        this.repository = repository;
    }

    public List<Reserva> getReservas() {
        return repository.findAll();
    }

    public Optional<Reserva> getReservaById(Long id) {
        return repository.findById(id);
    }
}