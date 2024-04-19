package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Livro;
import com.biblioteca.scbapi.model.repository.LivroRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class LivroService {
    private LivroRepository repository;

    public LivroService(LivroRepository repository) {
        this.repository = repository;
    }

    public List<Livro> getLivros() {
        return repository.findAll();
    }

    public Optional<Livro> getLivroById(Long id) {
        return repository.findById(id);
    }
}