package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Tomador;
import com.biblioteca.scbapi.model.repository.TomadorRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
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

    @Transactional
    public Tomador salvar(Tomador tomador) {
        validar(tomador);
        return repository.save(tomador);
    }

    @Transactional
    public void excluir(Tomador tomador) {
        Objects.requireNonNull(tomador.getId());
        repository.delete(tomador);
    }

    public void validar(Tomador tomador) {
        if (tomador.getEstado() == null || tomador.getEstado().isEmpty()) {
            throw new RegraNegocioException("Estado inválido");
        }
        if (tomador.getNumero() == null || tomador.getNumero().isEmpty()) {
            throw new RegraNegocioException("Numero inválido");
        }
        if (tomador.getLogradouro() == null || tomador.getLogradouro().isEmpty()) {
            throw new RegraNegocioException("Logradouro inválido");
        }
        if (tomador.getCidade() == null || tomador.getCidade().isEmpty()) {
            throw new RegraNegocioException("Cidade inválida");
        }
        if (tomador.getPais() == null || tomador.getPais().isEmpty()) {
            throw new RegraNegocioException("Pais inválido");
        }
        if (tomador.getNome() == null || tomador.getNome().isEmpty()) {
            throw new RegraNegocioException("Nome inválido");
        }
        if (tomador.getCpf() == null || tomador.getCpf().isEmpty()) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (tomador.getEmail() == null || tomador.getEmail().isEmpty()) {
            throw new RegraNegocioException("Email inválido");
        }
        if (tomador.getTelefone() == null || tomador.getTelefone().isEmpty()) {
            throw new RegraNegocioException("Telefone inválido");
        }
    }
}