package com.biblioteca.scbapi.model.repository;

import com.biblioteca.scbapi.model.entity.Exemplar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExemplarRepository extends JpaRepository<Exemplar, Long>{

}