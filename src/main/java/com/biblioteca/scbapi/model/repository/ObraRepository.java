package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Obra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ObraRepository extends JpaRepository<Obra, Long>{

}