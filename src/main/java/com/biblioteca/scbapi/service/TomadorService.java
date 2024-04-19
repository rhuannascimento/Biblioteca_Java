package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.model.repository.TomadorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class TomadorService {
    private TomadorRepository repository;

    public TomadorService(TomadorRepository repository) {
        this.repository = repository;
    }

    public List<Tomador> getTomadores() {
        return repository.findAll();
    }

    public Optional<Tomador> getTomadorById(Long id) {
        return repository.findById(id);
    }
}