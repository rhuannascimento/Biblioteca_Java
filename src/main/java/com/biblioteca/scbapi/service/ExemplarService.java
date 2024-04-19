package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.repository.ExemplarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
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
}