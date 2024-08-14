package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Exemplar;
import com.biblioteca.scbapi.model.entity.Obra;
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

    public List<Exemplar> getExemplarByObraId(Optional<Obra> obra) {
        return repository.findByObra(obra);
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


    public void excluirByObraId(Optional<Obra> obra){
        List<Exemplar> exemplares = getExemplarByObraId(obra);
        for(int i = 0; i < exemplares.size(); i++){
            excluir(exemplares.get(i));
        }
    }

    public void validar(Exemplar exemplar) {
        if (exemplar.getPrateleira() == null) {
            throw new RegraNegocioException("Prateleira em branco");
        }
    }
}