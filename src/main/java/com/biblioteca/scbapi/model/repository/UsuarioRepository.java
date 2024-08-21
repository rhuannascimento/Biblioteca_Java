package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>  {

}
