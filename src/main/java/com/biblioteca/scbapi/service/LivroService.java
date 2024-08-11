package com.biblioteca.scbapi.service;


import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Livro;
import com.biblioteca.scbapi.model.repository.LivroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Objects;
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

    public Livro salvar(Livro livro) {
        validar(livro);
        return repository.save(livro);
    }

    @Transactional
    public void excluir(Livro livro) {
        Objects.requireNonNull(livro.getId());
        repository.delete(livro);
    }
    
    public void validar(Livro livro) {
        if (livro.getObra() == null) {
            throw new RegraNegocioException("Obra em branco");
        }
        if (livro.getAutor() == null) {
            throw new RegraNegocioException("Autor em branco");
        }
        
    }
}