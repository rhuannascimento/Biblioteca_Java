package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Revista;
import com.biblioteca.scbapi.model.repository.RevistaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public Revista salvar(Revista revista) {
        validar(revista);
        return repository.save(revista);
    }

    @Transactional
    public void excluir(Revista revista) {
        Objects.requireNonNull(revista.getId());
        repository.delete(revista);
    }

    public void validar(Revista revista) {
        if (revista.getObra() == null) {
            throw new RegraNegocioException("Obra em branco");
        }
        if (revista.getEdicao() == null) {
            throw new RegraNegocioException("Edicao em branco");
        }
    }
}