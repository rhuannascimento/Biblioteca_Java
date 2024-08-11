package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.repository.ExemplarRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Service
public class ExemplarService {
    private ExemplarRepository repository;

    public ExemplarService(ExemplarRepository repository) {
        this.repository = repository;
    }

    public List<Exemplar> getExemplares() {
        return repository.findAll();
    }

    public Optional<Exemplar> getExemplarById(Long id) {
        return repository.findById(id);
    }

    public Exemplar salvar(Exemplar exemplar) {
        validar(exemplar);
        return repository.save(exemplar);
    }

    @Transactional
    public void excluir(Exemplar exemplar) {
        Objects.requireNonNull(exemplar.getId());
        repository.delete(exemplar);
    }

    public void validar(Exemplar exemplar) {
        if (exemplar.getPrateleira() == null) {
            throw new RegraNegocioException("Prateleira em branco");
        }
    }
}