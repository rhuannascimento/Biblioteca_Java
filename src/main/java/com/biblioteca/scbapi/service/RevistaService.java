package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Revista;
import com.biblioteca.scbapi.model.repository.RevistaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class RevistaService {
    private RevistaRepository repository;

    public RevistaService(RevistaRepository repository) {
        this.repository = repository;
    }

    public List<Revista> getRevistas() {
        return repository.findAll();
    }

    public Optional<Revista> getRevistaById(Long id) {
        return repository.findById(id);
    }
}