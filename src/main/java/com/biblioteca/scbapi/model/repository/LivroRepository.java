package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Livro;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LivroRepository extends JpaRepository<Livro, Long>{

}