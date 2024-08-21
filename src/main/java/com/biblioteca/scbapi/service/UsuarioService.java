package com.biblioteca.scbapi.service;

import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.model.entity.Usuario;
import com.biblioteca.scbapi.model.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class UsuarioService {
    private UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository) {
        this.repository = repository;
    }

    public List<Usuario> getUsuarios() {
        return repository.findAll();
    }

    public Optional<Usuario> getUsuarioById(Long id) {
        return repository.findById(id);
    }

    @Transactional
    public Usuario salvar(Usuario usuario) {
        validar(usuario);
        return repository.save(usuario);
    }

    @Transactional
    public void excluir(Usuario usuario) {
        Objects.requireNonNull(usuario.getId());
        repository.delete(usuario);
    }

    public void validar(Usuario usuario) {
        if (usuario.getCpf() == null || usuario.getCpf().isEmpty()) {
            throw new RegraNegocioException("CPF inválido");
        }
        if (usuario.getLogin() == null || usuario.getLogin().isEmpty()) {
            throw new RegraNegocioException("Login inválido");
        }
        if (usuario.getSenha() == null || usuario.getSenha().isEmpty()) {
            throw new RegraNegocioException("Login inválido");
        }
    }
}
