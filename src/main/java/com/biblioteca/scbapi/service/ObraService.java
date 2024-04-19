package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.repository.ObraRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class ObraService {
    private ObraRepository repository;

    public ObraService(ObraRepository repository) {
        this.repository = repository;
    }

    public List<Obra> getObras() {
        return repository.findAll();
    }

    public Optional<Obra> getObraById(Long id) {
        return repository.findById(id);
    }
}