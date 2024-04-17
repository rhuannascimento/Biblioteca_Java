package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long>{

}