package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Obra;
import com.biblioteca.scbapi.model.repository.ObraRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    public Obra salvar(Obra obra) {
        validar(obra);
        return repository.save(obra);
    }

    @Transactional
    public void excluir(Obra obra) {
        Objects.requireNonNull(obra.getId());
        repository.delete(obra);
    }

    public void validar(Obra obra) {
        if (obra.getCategoria() == null) {
            throw new RegraNegocioException("Categoria en branco");
        }
        if (obra.getTitulo() == null) {
            throw new RegraNegocioException("Titulo en branco");
        }
        if (obra.getEditora() == null) {
            throw new RegraNegocioException("Editora en branco");
        }
    }
}