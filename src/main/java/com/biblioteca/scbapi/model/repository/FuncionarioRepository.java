package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long>{

}