package com.biblioteca.scbapi.api.controller;

import com.biblioteca.scbapi.api.dto.CredenciaisDTO;
import com.biblioteca.scbapi.api.dto.TokenDTO;
import com.biblioteca.scbapi.api.dto.UsuarioDTO;
import com.biblioteca.scbapi.exception.RegraNegocioException;
import com.biblioteca.scbapi.exception.SenhaInvalidaException;
import com.biblioteca.scbapi.model.entity.Usuario;
import com.biblioteca.scbapi.security.JwtService;
import com.biblioteca.scbapi.service.UsuarioService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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

@RestController
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
@Api(value = "Usuário Controller", tags = {"Usuário"})
public class UsuarioController {

    private final UsuarioService service;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @GetMapping()
    @ApiOperation(value = "Lista todos os usuários", response = List.class)
    public ResponseEntity get() {
        List<Usuario> usuarios = service.getUsuarios();
        return ResponseEntity.ok(usuarios.stream().map(UsuarioDTO::create).collect(Collectors.toList()));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Obtém um usuário pelo ID", response = UsuarioDTO.class)
    public ResponseEntity get(
            @ApiParam(value = "ID do usuário", required = true) @PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(usuario.map(UsuarioDTO::create));
    }

    @PostMapping()
    @ApiOperation(value = "Cria um novo usuário", response = UsuarioDTO.class)
    public ResponseEntity post(
            @ApiParam(value = "Dados do usuário", required = true) @RequestBody UsuarioDTO dto) {
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
    @ApiOperation(value = "Autentica um usuário e gera um token", response = TokenDTO.class)
    public TokenDTO autenticar(
            @ApiParam(value = "Credenciais do usuário", required = true) @RequestBody CredenciaisDTO credenciais) {
        try {
            Usuario usuario = Usuario.builder()
                    .login(credenciais.getLogin())
                    .senha(credenciais.getSenha()).build();
            UserDetails usuarioAutenticado = service.autenticar(usuario);
            String token = jwtService.gerarToken(usuario);
            return new TokenDTO(usuario.getLogin(), token);
        } catch (UsernameNotFoundException | SenhaInvalidaException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @PutMapping("{id}")
    @ApiOperation(value = "Atualiza um usuário pelo ID", response = UsuarioDTO.class)
    public ResponseEntity atualizar(
            @ApiParam(value = "ID do usuário", required = true) @PathVariable("id") Long id,
            @ApiParam(value = "Dados atualizados do usuário", required = true) @RequestBody UsuarioDTO dto) {
        if (!service.getUsuarioById(id).isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
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
    @ApiOperation(value = "Deleta um usuário pelo ID")
    public ResponseEntity delete(
            @ApiParam(value = "ID do usuário", required = true) @PathVariable("id") Long id) {
        Optional<Usuario> usuario = service.getUsuarioById(id);
        if (!usuario.isPresent()) {
            return new ResponseEntity("Usuário não encontrado", HttpStatus.NOT_FOUND);
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
