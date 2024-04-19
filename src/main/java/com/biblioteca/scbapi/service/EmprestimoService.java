package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.model.entity.Emprestimo;
import com.biblioteca.scbapi.model.repository.EmprestimoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class EmprestimoService {
    private EmprestimoRepository repository;

    public EmprestimoService(EmprestimoRepository repository) {
        this.repository = repository;
    }

    public List<Emprestimo> getEmprestimos() {
        return repository.findAll();
    }

    public Optional<Emprestimo> getEmprestimoById(Long id) {
        return repository.findById(id);
    }
}