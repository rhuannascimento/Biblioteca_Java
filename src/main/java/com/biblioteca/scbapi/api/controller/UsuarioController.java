package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.CredenciaisDTO;
import com.biblioteca.scbapi.api.dto.TokenDTO;
import com.biblioteca.scbapi.api.dto.UsuarioDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.exception.SenhaInvalidaException;
import com.biblioteca.scbapi.model.entity.Usuario;
import com.biblioteca.scbapi.security.JwtService;
import com.biblioteca.scbapi.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/usuario")
@RestController
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @GetMapping()
    public ResponseEntity get() {
        List<Usuario> usuarios = service.getUsuarios();
        return ResponseEntity.ok(usuarios.stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    public ResponseEntity get(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Emprestimo não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario.map(UsuarioDTO::create));
    }

    @PostMapping()
    public ResponseEntity post(@RequestBody UsuarioDTO dto) {
        try {
            if (dto.getSenha() == null || dto.getSenha().trim().equals("") ||
                    dto.getSenhaRepeticao() == null || dto.getSenhaRepeticao().trim().equals("")) {
                return ResponseEntity.badRequest().body("Senha inválida");
            }
            if (!dto.getSenha().equals(dto.getSenhaRepeticao())) {
                return ResponseEntity.badRequest().body("Senhas não conferem");
            }
            Usuario usuario = converter(dto);
            String senhaCriptografada = passwordEncoder.encode(dto.getSenha());
            usuario.setSenha(senhaCriptografada);
            usuario = service.salvar(usuario);
            return new ResponseEntity(usuario, HttpStatus.CREATED);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/auth")
    public TokenDTO autenticar(@RequestBody CredenciaisDTO credenciais){
        try{
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = service.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e ){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{id}")
    public ResponseEntity atualizar(@PathVariable("id") Long id, @RequestBody UsuarioDTO dto) {
        if (!service.getUsuarioById(id).isPresent()) {
            return new ResponseEntity("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            Usuario usuario = converter(dto);
            usuario.setId(id);
            service.salvar(usuario);
            return ResponseEntity.ok(usuario);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("{id}")
    public ResponseEntity delete(@PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuario não encontrado", HttpStatus.NOT_FOUND);
        }
        try {
            service.excluir(usuario.get());
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        } catch (RegraNegocioException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    public Usuario converter(UsuarioDTO dto) {
        ModelMapper modelMapper = new ModelMapper();
        Usuario usuario = modelMapper.map(dto, Usuario.class);

        return usuario;
    }
}
