package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Emprestimo;
import com.biblioteca.scbapi.model.entity.Reserva;
import com.biblioteca.scbapi.model.repository.ReservaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public Reserva salvar(Reserva reserva) {
        validar(reserva);
        validarEmAtraso(reserva);
        return repository.save(reserva);
    }

    @Transactional
    public void excluir(Reserva reserva) {
        Objects.requireNonNull(reserva.getId());
        repository.delete(reserva);
    }

    public void validarEmAtraso(Reserva reserva) {
        if (reserva.getTomador().isEmAtarso()) {
            throw new RegraNegocioException("Tomador com pendencias");
        }
    }

    public void validar(Reserva reserva) {
        if (reserva.getObra() == null) {
            throw new RegraNegocioException("Obra em branco");
        }
        if (reserva.getTomador() == null) {
            throw new RegraNegocioException("Tomador em branco");
        }
    }
}